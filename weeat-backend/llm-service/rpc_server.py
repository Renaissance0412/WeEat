import os
import sys
from concurrent import futures
import time
import uuid

import grpc
import etcd3
import threading
import yaml

from rpc import llm_service_pb2, llm_service_pb2_grpc
from service import llm_service
import argparse


LOCAL_PATH = os.path.dirname(os.path.abspath(__file__))
sys.path.append(os.path.join(LOCAL_PATH, ".."))
sys.path.append(os.path.dirname(os.path.abspath(__file__)) + "/rpc")


def register_with_etcd(etcd_host, etcd_port, etcd_key, etcd_value, ttl=10):
    etcd = etcd3.client(host=etcd_host, port=etcd_port)
    key = etcd_key + "/" + str(uuid.uuid4().int)
    value = etcd_value
    lease = etcd.lease(ttl)
    etcd.put(key, value, lease)
    print(f"Registered {value} with key {key} in etcd.")

    try:
        while True:
            lease.refresh()
            print(f"Lease for key {key} refreshed with TTL {ttl} seconds.")
            time.sleep(ttl / 2)
    except KeyboardInterrupt:
        print("Stopping keepalive.")
    finally:
        lease.revoke()


def serve(
    host,
    port,
    api_key="EMPTY",
    api_base="http://localhost:8000/v1",
    model="facebook/opt-125m",
):
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    llm_service_pb2_grpc.add_LLMServiceServicer_to_server(
        llm_service.LLMService(api_key, api_base, model), server
    )
    # server.add_insecure_port('[::]:50051')
    server.add_insecure_port("{}:{}".format(host, port))
    server.start()
    print("Server started on {}:{}".format(host, port))
    try:
        while True:
            time.sleep(86400)
    except KeyboardInterrupt:
        server.stop(0)


def parse_args():
    parser = argparse.ArgumentParser(description="RPC Server")
    parser.add_argument(
        "--config", type=str, default="config.yaml", help="Path to config file"
    )
    return parser.parse_args()


if __name__ == "__main__":
    args = parse_args()
    config_file = args.config

    with open(config_file, "r") as f:
        config = yaml.safe_load(f)

    print(f"Loaded config: {config}")

    register_key = config["ContainerName"] + ":" + str(config["Port"])
    etcd_thread = threading.Thread(
        target=register_with_etcd,
        args=(
            config["ETCD"]["Host"],
            config["ETCD"]["Port"],
            config["Name"],
            register_key,
        ),
    )
    etcd_thread.start()

    serve(
        config["Host"],
        config["Port"],
        config["OPENAI"]["API_KEY"],
        config["OPENAI"]["API_BASE"],
        config["OPENAI"]["MODEL"],
    )
