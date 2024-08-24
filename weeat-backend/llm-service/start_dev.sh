#!/bin/bash

export CUDA_VISIBLE_DEVICES=1

HF_ENDPOINT=https://hf-mirror.com vllm serve facebook/opt-125m --chat-template template_chatml.jinja &

# wait for vllm is ready
while ! curl -s http://localhost:8000/health > /dev/null; do
  sleep 1
done
PROTOCOL_BUFFERS_PYTHON_IMPLEMENTATION=python python rpc_server.py --config config_dev.yaml &
wait