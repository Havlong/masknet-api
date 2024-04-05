from PyDantic import Model
from typing import Dict, List


class Request(Model):
    embedded_params: Dict[str, int]
    numerical_params: Dict[str, float]
    text_params: Dict[str, str]


class Params(Model):
    embedded_params: List[str]
    numerical_params: List[str]
    text_params: List[str]


class Response(Model):
    probabilities: List[float]
