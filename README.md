# masknet-api
## Backend for Deep Neural Network based on `MaskNet` architecture

- `dnn-back` contains Spring Boot application that collects requests, saves them and manages Neural Network
- `dnn-python` contains Keras model that is trained on TensorFlow and is managed through by FastAPI
- Secrets are distributed through GitHub Actions, but `docker-compose.yml` might need to be changed at safe environment after build
- MongoDB is used by Backend to store Request data, and is managed by Spring Boot Data
