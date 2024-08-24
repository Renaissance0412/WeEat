#!/bin/bash

export CUDA_VISIBLE_DEVICES=1

PROTOCOL_BUFFERS_PYTHON_IMPLEMENTATION=python python rpc_server.py --config config_prod.yaml &
wait