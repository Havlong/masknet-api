from drk_data import prep_embeddings
from back.models import Request
from model.mask_net import MaskNet


class ModelManager:
    def __init__(self):
        self.categorical_feats, self.numerical_feats, self.vocabs = prep_embeddings()
        self.vocab_sizes = [len(vocab) for vocab in self.vocabs]
        self.model = MaskNet()

    def params(self):
        pass 


    def feedforward(self, input: Request):
        data = {}
        for feat_name in self.categorical_feats:
            pass


    def retrain(self):
        pass
