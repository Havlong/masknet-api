FROM tensorflow/tensorflow:latest-gpu

COPY . /tf/app
WORKDIR /tf/app

RUN ["python3", "-m", "pip", "install", "--upgrade", "pip"]
RUN ["python3", "-m", "pip", "install", "--upgrade", "keras"]
RUN ["python3", "-m", "pip", "install", "-r", "requirements.txt"]

# Pass argument with command
ENTRYPOINT ["uvicorn", "masknet.main:app", "--host", "0.0.0.0", "--port"]
