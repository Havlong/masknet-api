import uvicorn
from fastapi import FastAPI, HTTPException

from masknet.drk_model import ModelManager
from masknet.dto import Request, Response, Params

app = FastAPI()
manager = ModelManager()


@app.get('/params', status_code=200)
def get_params() -> Params:
    return manager.params()


@app.get('/retrain', status_code=201)
def retrain():
    try:
        manager.retrain()
    except Exception as error:
        raise HTTPException(status_code=500, detail=str(error))


@app.post('/request', status_code=200)
def feed_forward(request: Request) -> Response:
    if not (manager.validate(request)):
        raise HTTPException(status_code=400)
    try:
        return manager.feedforward(request)
    except Exception as error:
        raise HTTPException(status_code=500, detail=str(error))


if __name__ == '__main__':
    uvicorn.run('masknet.main:app', port=8081)
