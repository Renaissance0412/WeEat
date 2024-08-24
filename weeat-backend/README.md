# WeEat Backend

## Description

This is the backend of the WeEat project. It is a REST API that provides the necessary endpoints to manage the data of the WeEat project built with TiDB, this project contains the following endpoints:

- 💻 `/api-service` export necessary apis for client and interact with backend personal TiDB service.
- 🍔 `/asr-service` support audio to text and text to audio conversion grpc endpoint.
- 📦 `/llm-service` support local llm service.
- 🚀 `/rag-service` support personal, LRU-like memory rag service, and web search agent for details information.
- 📚 `/recognition-service` support image/video analysis.

## Prerequisites

1. Go 1.21
2. Docker 27.0.3
3. Python 3.8 or later
4. MiniConda 24.4.0
5. Update each service config in `xxx-service/etc/xxx.yml` file, you may need to get a free bing api if you want to use the **web search agent**.
6. Better to have a GPU for for deploying the model(Current is A100+GPU)
7. Download `Meta-Llama-3.1-8B-Instruct`, `whisper` small, `maidalun1020/bce-embedding-base_v1` from `huggingface` and put them in `~/.cache/huggingface/hub/` folder, like:

```bash
ll ~/.cache/huggingface/hub/
models--Meta-Llama-3.1-8B-Instruct
models--maidalun1020--bce-embedding-base_v1
models--facebook--opt-125m
```

especially for `whisper`, you need to download it and put it in the `~/.cache/whisper/` folder.

Prepare conda environment for development:

```bash
conda env create -f llm-environment.yml
conda env create -f vllm-environment.yml
```

## For Development

```bash
docker compose up -d
```

Check the logs to see if the etcd server is running:

1. Run `api-service`:

```bash
cd api-service
go mod tidy
go run weeat.go -f etc/weeat-api-dev.yaml
```

2. Run `asr-service`:

```bash
cd asr-service
PROTOCOL_BUFFERS_PYTHON_IMPLEMENTATION=python python rpc_server.py --config config_dev.yaml
```

3. Run `llm-service`:

```bash
cd llm-service
vllm serve Meta-Llama-3.1-8B-Instruct --dtype auto
PROTOCOL_BUFFERS_PYTHON_IMPLEMENTATION=python python rpc_server.py --config config_dev.yaml
```

4. Run `rag-service`:

```bash
cd rag-service
PROTOCOL_BUFFERS_PYTHON_IMPLEMENTATION=python python rpc_server.py --config config_dev.yaml
```

5. Run `recognition-service`:

```bash
cd recognition-service
PROTOCOL_BUFFERS_PYTHON_IMPLEMENTATION=python python rpc_server.py --config config_dev.yaml
```

And then you can access the API service at `http://localhost:8089`(If you do not change the port in the `weeat-api-dev.yaml` file).

API doc is in `/api-service/weeat.json`, it is an OpenAPI 3.0 file.

## For Deployment

You can build `Dockerfile` in each service folder and deploy them to the server, reference is `docker-compose-prod.yml`, like:

```bash
docker buildx build -t weeat-backend-xxx-service -f Dockerfile .
```

> Tips: this appropach need nvidia-tools in docker and download the pretrained models in host server, please check the `Dockerfile` for more details.

And then you can access the API service at `http://localhost:8089`(If you do not change the port in the `weeat-api-dev.yaml` file).

API doc is in `/api-service/weeat.json`, it is an OpenAPI 3.0 file.