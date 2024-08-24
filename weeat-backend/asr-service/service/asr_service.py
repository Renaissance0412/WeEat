from datetime import timedelta
import io
import os
import sys
import whisper
import requests
import logging

import uuid
from PIL import Image
import numpy as np

from rpc import asr_service_pb2, asr_service_pb2_grpc

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir)))


class ASRService(asr_service_pb2_grpc.ASRServiceServicer):
    def __init__(self, model="small"):
        self.model = whisper.load_model(model)
        logging.info(f"model: {self.model}, model size: {model}")

    def StreamRecognize(self, request, context):
        """ASR with stream response method"""
        # For PCM decode
        # for audio_chunk in request.audio:
        #     audio = np.frombuffer(audio_chunk, dtype=np.int16)
        #     result = self.model.transcribe(audio)
        #     response = asr_service_pb2.StreamRecognizeResponse()
        #     response.transcript = result["text"]
        #     yield response

        # For general decode, we just save the audio to a file and decode it by ffmpeg
        audio_file = f"tmp_{uuid.uuid4()}.mp3"
        with open(audio_file, "wb") as f:
            f.write(request.audio)

        chunk_size = 16000
        audio_data = request.audio

        for i in range(0, len(audio_data), chunk_size * 2):  # 2 bytes per int16 sample
            audio_chunk = audio_data[i : i + chunk_size * 2]

            for result in self.pseudo_stream_transcribe(
                audio_chunk, chunk_size=chunk_size
            ):
                response = asr_service_pb2.ASRResponse()
                response.transcript = result
                yield response

    def pseudo_stream_transcribe(self, audio_chunk, chunk_size=16000):
        """pseudo stream transcribe for whisper model"""
        buffer = np.array([], dtype=np.int16)

        buffer = np.concatenate((buffer, np.frombuffer(audio_chunk, dtype=np.int16)))

        if len(buffer) >= chunk_size:
            float_buffer = buffer[:chunk_size].astype(np.float32) / 32768.0
            result = self.model.transcribe(float_buffer)
            yield result["text"]

            buffer = buffer[chunk_size:]

        if len(buffer) > 0:
            float_buffer = buffer.astype(np.float32) / 32768.0
            result = self.model.transcribe(float_buffer)
            yield result["text"]

    def SyncRecognize(self, request, context):
        """ASR with sync response method"""
        audio_file = f"tmp_{uuid.uuid4()}.mp3"
        with open(audio_file, "wb") as f:
            f.write(request.audio)

        result = self.model.transcribe(audio_file)
        response = asr_service_pb2.ASRResponse()
        response.transcript = result["text"]

        # remove this temp file
        os.remove(audio_file)

        return response
