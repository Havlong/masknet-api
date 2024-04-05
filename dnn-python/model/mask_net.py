from typing import List

import keras
from keras import layers, Input, Model
from mask_block import MaskBlock
from embeddings import DenseEmbedding, EmbeddingsProcessor


def MaskNet(categorical_feats: List[str], vocab_sizes: List[int], numerical_feats: List[str], n_outputs: int, is_serial: bool):
    if is_serial:
        mask_block_units = [1024, 1536, 2048, 1536, 1024]
    else:
        mask_block_units = [1024 for _ in range(5)]
    projection_units = 1024
    embeddings_len = 32

    emb_inputs = [Input(shape=(1,), name=feat_name) for feat_name in categorical_feats]
    proj_inputs = [Input(shape=(1,), name=feat_name) for feat_name in numerical_feats]

    # Embedded categorical inputs
    embeddings = [layers.Embedding(vocab_sizes[feat_idx], embeddings_len)(emb_input) for feat_idx, emb_input in enumerate(emb_inputs)]
    # Emulate embeddings with learnable vectors of parameters
    projections = [DenseEmbedding(embeddings_len)(proj_input) for proj_input in proj_inputs]
    
    emb = layers.concatenate([*embeddings, *projections], axis=1)
    
    x = EmbeddingsProcessor()(emb)
    emb = layers.Flatten()(emb)
    
    if is_serial:
        for block_units in mask_block_units:
            x = MaskBlock(block_units)([x, emb])
    else:
        x = layers.concatenate([MaskBlock(block_units)([x, emb]) for block_units in mask_block_units])
        x = layers.Dense(projection_units, activation='relu')(x)
        x = layers.Dense(projection_units // 4, activation='relu')(x)

    outputs = layers.Dense(n_outputs)(x)

    mask_net_model = Model(inputs=[*emb_inputs, *proj_inputs], outputs=outputs, name='SerialMaskNet' if is_serial else 'ParallelMaskNet')

    mask_net_model.compile(
        optimizer=keras.optimizers.Adam(learning_rate=1e-4),
        loss=keras.losses.BinaryCrossentropy(from_logits=True),
        metrics=[
            'accuracy',
            keras.metrics.Recall(thresholds=0),
            keras.metrics.AUC(name='roc_auc', curve='ROC', from_logits=True),
            keras.metrics.AUC(name='prc_auc', curve='PR', from_logits=True)
        ]
    )

    return mask_net_model
    
