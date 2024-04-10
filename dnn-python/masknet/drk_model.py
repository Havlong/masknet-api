import os

from keras.callbacks import EarlyStopping, ModelCheckpoint, TensorBoard
from keras.ops import sigmoid, convert_to_tensor, convert_to_numpy
from keras.saving import load_model

from masknet.drk_data import prep_embeddings, preprocess_data, prep_train_dataset
from masknet.dto import Request, Params, Response
from masknet.model.mask_net import MaskNet

os.environ["KERAS_BACKEND"] = "tensorflow"
model_name = 'parallel-masknet'
model_path = os.path.join('out', f'{model_name}.keras')
logs_path = os.path.join('out', 'logs', model_name)


class ModelManager:
    def __init__(self):
        self.categorical_feats, self.numerical_feats, self.vocabs = prep_embeddings()
        self.vocab_sizes = [len(vocab) for vocab in self.vocabs]
        self.load_model()

    def load_model(self):
        if not os.path.exists(model_path):
            self.retrain()
        self.model = load_model(model_path)

    def params(self) -> Params:
        return Params(embedded_params=list(self.categorical_feats.keys()),
                      numerical_params=self.numerical_feats)

    def validate(self, input: Request) -> bool:
        for feat_name in self.categorical_feats:
            if feat_name not in input.embedded_params:
                return False
        for feat_name in self.numerical_feats:
            if feat_name not in input.numerical_params:
                return False
        return True

    def feedforward(self, input: Request) -> Response:
        data = {}

        for feat_name, data_type in self.categorical_feats:
            if data_type == 'float64':
                data[feat_name] = float(input.embedded_params[feat_name])
            elif data_type == 'int64':
                data[feat_name] = int(input.embedded_params[feat_name])
            else:
                data[feat_name] = input.embedded_params[feat_name]

        for feat_name in self.numerical_feats:
            data[feat_name] = input.numerical_params[feat_name]

        data = preprocess_data(data, self.categorical_feats, self.numerical_feats, self.vocabs)

        predictions = self.model(data, training=False)
        probs = sigmoid(convert_to_tensor(predictions))

        return Response(probabilities=convert_to_numpy(probs).tolist())

    def retrain(self) -> None:
        train_ds = prep_train_dataset(self.categorical_feats, self.numerical_feats, self.vocabs)
        new_model = MaskNet(
            categorical_feats=self.categorical_feats,
            vocab_sizes=self.vocab_sizes,
            numerical_feats=self.numerical_feats,
            n_outputs=1,
            is_serial=False
        )

        model_callbacks = [
            EarlyStopping(patience=10),
            TensorBoard(log_dir=logs_path),
            ModelCheckpoint(model_path, monitor='accuracy', mode='max', save_best_only=True)
        ]

        new_model.fit(train_ds, epochs=20, callbacks=model_callbacks)
