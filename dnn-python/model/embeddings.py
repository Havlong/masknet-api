from keras import layers, ops
from keras.saving import register_keras_serializable


@register_keras_serializable()
class DenseEmbedding(layers.Layer):
    def __init__(self, emb_len, **kwargs):
        super().__init__(**kwargs)
        self.emb_len = emb_len
        
        self.reshaper = layers.Reshape((-1, 1))
        self.projector = layers.Dense(self.emb_len, use_bias=False)


    def build(self, input_shape):
        self.reshaper.build(input_shape)
        shape = self.reshaper.compute_output_shape(input_shape)
        self.projector.build(shape)


    def call(self, inputs):
        return self.projector(self.reshaper(inputs))

    
    def get_config(self):
        config = super().get_config()
        
        config.update({
            "emb_len": self.emb_len
        })
        
        return config


@register_keras_serializable()
class EmbeddingsProcessor(layers.Layer):
    def build(self, input_shape):
        self.num_feats, self.emb_len = input_shape[-2:]
        self.normalizers = [layers.LayerNormalization() for _ in range(self.num_feats)]
        self.flatten = layers.Flatten()


    def call(self, inputs):
        splits = ops.split(inputs, self.num_feats, axis=-2)
        outputs = layers.concatenate([self.normalizers[feat](split) for feat, split in enumerate(splits)], axis=-2)
        return self.flatten(outputs)
