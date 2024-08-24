from datetime import timedelta
import io
import os
import sys
from openai import OpenAI
import requests
import logging

import uuid
from PIL import Image
import numpy as np

from rpc import llm_service_pb2, llm_service_pb2_grpc

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir)))
logging.basicConfig(stream=sys.stdout, level=logging.INFO)
logger = logging.getLogger()

DIET_SYSTEM_PROMPT = """
You are an assistant specializing in health management, tasked with providing recipe suggestions based on the user's available ingredients, dietary preferences, and health condition. Your output should be limited to health-related recipes and must be formatted as JSON. Each recipe should include the dish name ("name"), a detailed description of the dish ("desc"), and the estimated preparation time ("time").

Output format:
- The output should be a JSON-formatted list, for example:
  [
    {
      "name": "Dish Name",
      "desc": "A detailed description of the dish, including key ingredients, cooking methods, and health benefits.",
      "time": "Estimated preparation time, e.g., '15 minutes'"
    }
  ]

Reference information:
- Refrigerator contents: 2 tomatoes, 5 eggs, 1 mango, 5 green peppers, 1 portion of meat
- Dietary preference: Prefers light meals today
- Health record: The user had previous digestive issues and has referred to shrimp congee as a suitable dish

Generation requirements:
1. The recommended recipes should prioritize the user's available ingredients and match their current dietary preferences and health condition.
2. The suggested dishes should focus on light, easy-to-digest food options.
3. Include cooking tips and ingredient combinations that are suitable for digestive health in the description.
4. Recipe descriptions should be concise and include health benefits.

Please generate recipe suggestions based on this information.
"""


class LLMService(llm_service_pb2_grpc.LLMServiceServicer):
    def __init__(
        self,
        api_key="EMPTY",
        api_base="http://localhost:8000/v1",
        model="facebook/opt-125m",
    ):
        self.client = OpenAI(
            api_key=api_key,
            base_url=api_base,
        )
        self.model = model
        logger.info(f"client: {self.client}, api_key: {api_key}, api_base: {api_base}")

    def StreamChat(self, request, context):
        """Chat with stream response method"""
        # convert history to messages
        history_messages = []
        for message in request.history:
            history_messages.append({"role": "user", "content": message.message})

        # add current question
        history_messages.append({"role": "user", "content": request.message})

        chat_response = self.client.chat.completions.create(
            model=self.model,
            messages=[
                {"role": "system", "content": DIET_SYSTEM_PROMPT},
                *history_messages,
            ],
            stream=True,
        )

        # return stream response
        for chunk in chat_response:
            yield llm_service_pb2.ChatMessageResponse(
                message=chunk.choices[0].delta.content
            )

    def SyncChat(self, request, context):
        """Chat with sync response method"""
        # convert history to messages
        history_messages = []
        for message in request.history:
            history_messages.append({"role": "user", "content": message.message})

        # add current question
        history_messages.append({"role": "user", "content": request.message})

        chat_response = self.client.chat.completions.create(
            model=self.model,
            messages=[
                {"role": "system", "content": DIET_SYSTEM_PROMPT},
                *history_messages,
            ],
        )

        return llm_service_pb2.ChatMessageResponse(
            message=chat_response.choices[0].message.content
        )
