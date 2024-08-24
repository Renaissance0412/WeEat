FROM nvidia/cuda:12.1.0-cudnn8-devel-ubuntu22.04

# prevent interactive dialog
ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y \
    python3.10 \
    python3.10-venv \
    python3.10-distutils \
    python3.10-dev \
    python3-pip \
    ffmpeg \
    git \
    libgl1-mesa-glx \
    libglib2.0-0 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# make python3.10 the default python
# RUN python3.10 -m venv /opt/venv
# ENV PATH="/opt/venv/bin:$PATH"

COPY requirements.txt /app/
RUN pip3 install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple

# RUN pip3 install --no-cache-dir -r requirements.txt
# RUN pip3 install -r requirements.txt

COPY . /app

EXPOSE 50051

ENV NVIDIA_VISIBLE_DEVICES=all
ENV NVIDIA_DRIVER_CAPABILITIES=compute,utility
ENV PROTOCOL_BUFFERS_PYTHON_IMPLEMENTATION=python

WORKDIR /app

CMD ["bash", "start.sh"]