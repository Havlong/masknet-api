from pydantic import BaseModel, Field

from typing import Dict, List


class Request(BaseModel):
    embedded_params: Dict[str, str] = Field(
        default={}, title='Embedded parameters',
        description='Categorical values that are embedded before feeding into network'
    )
    numerical_params: Dict[str, float] = Field(
        default={}, title='Numerical parameters',
        description='Values that are fed into the network right away'
    )


class Params(BaseModel):
    embedded_params: List[str] = Field(
        default=[], title='Embedded parameters',
        description='List of params that are embedded before feeding into network'
    )
    numerical_params: List[str] = Field(
        default=[], title='Numerical parameters',
        description='List of params that are fed into the network right away'
    )


class Response(BaseModel):
    probabilities: List[float] = Field(
        title='Probabilities',
        description='List of probabilities for each class'
    )
