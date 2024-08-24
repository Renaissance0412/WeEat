from datetime import timedelta
import io
import os
import sys
import requests
import logging

import uuid
from PIL import Image
import numpy as np
import torch
from utils.general import non_max_suppression, scale_boxes, xyxy2xywh
from models.common import DetectMultiBackend
from utils.torch_utils import select_device
from utils.augmentations import letterbox

from rpc import recognition_service_pb2, recognition_service_pb2_grpc

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir)))
logging.basicConfig(stream=sys.stdout, level=logging.INFO)
logger = logging.getLogger()


class RecognitionService(recognition_service_pb2_grpc.RecognitionServiceServicer):
    def __init__(self, model, conf_threshold=0.25, iou_threshold=0.45):
        self.device = select_device("")  # Automatically select best device (CPU/GPU)
        self.model = DetectMultiBackend(model, device=self.device)  # Load YOLOv5 model
        self.conf_threshold = conf_threshold
        self.iou_threshold = iou_threshold
        logger.info(
            f"Model loaded: {model}, conf_threshold: {conf_threshold}, iou_threshold: {iou_threshold}"
        )

    def Recognize(self, request, context):
        """Recognize image and return recognition result"""
        # Convert the input image (bytes) to a PIL Image
        image = request.image
        image = Image.open(io.BytesIO(image)).convert("RGB")
        image_np = np.array(image)

        # Read from file
        # image_url = "test6.png"
        # image_np = np.array(Image.open(image_url).convert('RGB'))

        # Preprocess the image
        img = letterbox(image_np, 640, stride=32, auto=True)[0]  # padded resize
        img = img.transpose((2, 0, 1))[::-1]  # HWC to CHW, BGR to RGB
        img = np.ascontiguousarray(img)  # contiguous
        img = torch.from_numpy(img).to(self.device)
        img = img.float() / 255.0  # Normalize to 0-1
        if img.ndimension() == 3:
            img = img.unsqueeze(0)  # Add batch dimension

        # Run inference
        pred = self.model(img)

        # Apply Non-Max Suppression (NMS)
        pred = non_max_suppression(pred, self.conf_threshold, self.iou_threshold)

        # Process predictions
        items = []
        for det in pred:
            if len(det):
                # Rescale boxes to original image size
                det[:, :4] = scale_boxes(
                    img.shape[2:], det[:, :4], image_np.shape
                ).round()

                for *xyxy, conf, cls in reversed(det):
                    name = self.model.names[int(cls)]
                    confidence = float(conf)
                    items.append(
                        recognition_service_pb2.RecognitionResponseItem(
                            name=name, confidence=confidence
                        )
                    )

        # Create the response
        response = recognition_service_pb2.RecognitionResponse(
            items=items, count=len(items)
        )
        return response
