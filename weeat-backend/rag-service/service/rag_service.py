from datetime import datetime, timedelta
import io
import json
import os
import sys
from openai import OpenAI
import whisper
import requests
import logging

import uuid
from PIL import Image
import numpy as np
from sqlalchemy import create_engine, inspect
from sqlalchemy.orm import Session
from BCEmbedding import EmbeddingModel
from llama_index.core import VectorStoreIndex, StorageContext
from llama_index.vector_stores.tidbvector import TiDBVectorStore  # type: ignore
from llama_index.readers.web import SimpleWebPageReader

from rpc import rag_service_pb2, rag_service_pb2_grpc
from models import user, user_session, session_history, web_agent_history, Base
from utils import cache_manager, code, web_search

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir)))
logging.basicConfig(stream=sys.stdout, level=logging.INFO)
logger = logging.getLogger()

DIET_SYSTEM_PROMPT = """
You are an assistant specializing in health management, tasked with providing recipe suggestions based on the user's available ingredients, dietary preferences, and health condition. Your output should be limited to health-related recipes and must be formatted as JSON. Each recipe should include the dish name ("name"), a detailed description of the dish ("desc"), and the estimated preparation time ("time").

Output format:
- The output must be a JSON-formatted list, for example:
  [
    {
      "name": "Dish Name",
      "desc": "A detailed description of the dish, including key ingredients, cooking methods, and health benefits.",
      "time": "Estimated preparation time, e.g., '15 minutes'"
    }
  ]

Reference information:
- Refrigerator contents: REFRIGERATOR_CONTENTS
- Dietary preference: PREFERS_LIGHT_MEALS
- Web search result: WEB_SEARCH_RESULT

Generation requirements:
1. The recommended recipes should prioritize the user's available ingredients and match their current dietary preferences and health condition.
2. The suggested dishes should focus on light, easy-to-digest food options.
3. Include cooking tips and ingredient combinations that are suitable for digestive health in the description.
4. Recipe descriptions should be concise and include health benefits.

Please generate recipe suggestions based on this information.
"""


class RAGService(rag_service_pb2_grpc.RAGServiceServicer):
    def __init__(
        self,
        tidb_base_url,
        create_table=False,
        api_key="EMPTY",
        api_base="http://localhost:8000/v1",
        model="facebook/opt-125m",
        embedding_model="maidalun1020/bce-embedding-base_v1",
        bing_api_key=None,
        bing_api_endpoint=None,
    ):
        self.engine = create_engine(tidb_base_url)
        # Create tables if not exists
        with Session(self.engine) as session:
            inspector = inspect(self.engine)
            tables = inspector.get_table_names()

            # if not all([table in tables for table in Base.metadata.tables.keys()]):
            if create_table:
                Base.metadata.create_all(self.engine)
                logger.info(f"Created tables: {Base.metadata.tables.keys()}")
            else:
                logger.info(f"Tables already exist: {tables}")
        self.client = OpenAI(
            api_key=api_key,
            base_url=api_base,
        )
        self.model = model
        logger.info(f"client: {self.client}, api_key: {api_key}, api_base: {api_base}")
        self.embedding_model = EmbeddingModel(model_name_or_path=embedding_model)
        logger.info(f"embedding_model: {self.embedding_model}")

        self.lru_cache = cache_manager.CacheManager(
            max_size=100, callback=self.write_back_task
        )
        self.system_prompt_cache = cache_manager.CacheManager(max_size=100)
        # Enable bing web search if bing_api_key and bing_api_endpoint are provided
        if bing_api_key and bing_api_endpoint:
            logger.info(
                f"bing_api_key: {bing_api_key}, bing_api_endpoint: {bing_api_endpoint}"
            )
            self.bing_search = web_search.BingSearch(bing_api_key, bing_api_endpoint)
        else:
            self.bing_search = None

    def StreamChat(self, request, context):
        """Chat with stream response method"""
        history_messages = []

        # Check current system prompt from LRU cache
        system_prompt = self.system_prompt_cache.get_cache(
            request.user_id, request.context_id
        )
        if system_prompt:
            history_messages.append({"role": "system", "content": system_prompt})
        else:
            system_prompt = self.prepare_prompt(request)
            history_messages.append({"role": "system", "content": system_prompt})
            # Update cache result
            self.system_prompt_cache.set_cache(
                request.user_id, request.context_id, system_prompt
            )
        history_messages.append({"role": "system", "content": system_prompt})

        # Check current chat history from LRU cache
        cache_result = self.lru_cache.get_cache(request.user_id, request.context_id)
        if cache_result:
            for (
                question,
                question_embedding,
                answer,
                answer_embedding,
                created_at,
            ) in cache_result:
                history_messages.append({"role": "user", "content": question})
                history_messages.append({"role": "user", "content": answer})

        chat_response = self.client.chat.completions.create(
            model=self.model,
            messages=history_messages,
            stream=True,
        )

        # return stream response
        full_response = []
        for chunk in chat_response:
            content = chunk.choices[0].delta.content
            if content:
                full_response.append(content)

            yield rag_service_pb2.ChatMessageResponse(message=content)

        # Save chat history to LRU cache
        # Clean none in case of empty string
        complete_message = "".join(filter(None, full_response))
        request_message_embedding = self.embedding_model.encode(request.message)[0]
        response_message_embedding = self.embedding_model.encode(complete_message)[0]
        create_at = datetime.now()
        if cache_result:
            cache_result.append(
                (
                    request.message,
                    request_message_embedding,
                    complete_message,
                    response_message_embedding,
                    create_at,
                )
            )
            self.lru_cache.set_cache(request.user_id, request.context_id, cache_result)
        else:
            self.lru_cache.set_cache(
                request.user_id,
                request.context_id,
                [
                    (
                        request.message,
                        request_message_embedding,
                        complete_message,
                        response_message_embedding,
                        create_at,
                    )
                ],
            )

    def SyncChat(self, request, context):
        """Chat with sync response method"""
        history_messages = []

        # Check current system prompt from LRU cache
        system_prompt = self.system_prompt_cache.get_cache(
            request.user_id, request.context_id
        )
        if system_prompt:
            history_messages.append({"role": "system", "content": system_prompt})
        else:
            system_prompt = self.prepare_prompt(request)
            logger.info(f"system_prompt: {system_prompt}")
            history_messages.append({"role": "system", "content": system_prompt})
            # Update cache result
            self.system_prompt_cache.set_cache(
                request.user_id, request.context_id, system_prompt
            )
        history_messages.append({"role": "system", "content": system_prompt})

        # Check current chat history from LRU cache
        cache_result = self.lru_cache.get_cache(request.user_id, request.context_id)
        if cache_result:
            for (
                question,
                question_embedding,
                answer,
                answer_embedding,
                created_at,
            ) in cache_result:
                history_messages.append({"role": "user", "content": question})
                history_messages.append({"role": "user", "content": answer})

        # Add current question
        history_messages.append({"role": "user", "content": request.message})

        chat_response = self.client.chat.completions.create(
            model=self.model,
            messages=history_messages,
        )

        # Save chat history to LRU cache
        request_message_embedding = self.embedding_model.encode(request.message)[0]
        response_message_embedding = self.embedding_model.encode(
            chat_response.choices[0].message.content
        )[0]
        create_at = datetime.now()
        if cache_result:
            cache_result.append(
                (
                    request.message,
                    request_message_embedding,
                    chat_response.choices[0].message.content,
                    response_message_embedding,
                    create_at,
                )
            )
            self.lru_cache.set_cache(request.user_id, request.context_id, cache_result)
        else:
            self.lru_cache.set_cache(
                request.user_id,
                request.context_id,
                [
                    (
                        request.message,
                        request_message_embedding,
                        chat_response.choices[0].message.content,
                        response_message_embedding,
                        create_at,
                    )
                ],
            )

        # Return chat response
        return rag_service_pb2.ChatMessageResponse(
            message=chat_response.choices[0].message.content
        )

    def CreateUser(self, request, context):
        """Create new user"""
        # gen user id(string) by uuid
        user_id = str(uuid.uuid4())
        # save user to database
        try:
            with Session(self.engine) as session:
                session.add(
                    user.User(
                        uuid=user_id,
                        username=request.username,
                        token=request.token,
                        interests=request.interests,
                        # get first dim
                        interests_embedding=self.embedding_model.encode(
                            request.interests
                        )[0],
                    )
                )
                session.commit()
                logger.info(f"Created user: {user_id}")
        except Exception as e:
            logger.error(f"Failed to create user: {e}")
            return rag_service_pb2.CreateUserResponse(
                user_id=None,
            )

        return rag_service_pb2.CreateUserResponse(
            user_id=user_id,
        )

    def CheckUserValid(self, request, context):
        """Check user valid"""
        # check userid and token is in db
        try:
            with Session(self.engine) as session:
                result = (
                    session.query(user.User)
                    .filter_by(uuid=request.user_id, token=request.token)
                    .first()
                )
                if result:
                    logger.info(f"Check user valid: {request.user_id} is valid")
                    return rag_service_pb2.CheckUserResponse(
                        valid=True,
                    )
                else:
                    logger.info(f"Check user valid: {request.user_id} is invalid")
                    return rag_service_pb2.CheckUserResponse(
                        valid=False,
                    )
        except Exception as e:
            logger.error(f"Failed to check user valid: {e}")
            return rag_service_pb2.CheckUserResponse(
                valid=False,
            )

    def CreateChatContext(self, request, context):
        """Create new chat context"""
        # generate chat context id(string) by uuid
        chat_context_id = str(uuid.uuid4())
        # save chat context to database
        try:
            with Session(self.engine) as session:
                session.add(
                    user_session.UserSession(
                        user_uuid=request.user_id,
                        session_id=chat_context_id,
                    )
                )
                session.commit()
                logger.info(
                    f"Created user: {request.user_id} with chat context: {chat_context_id}"
                )
        except Exception as e:
            logger.error(f"Failed to create chat context: {e}")
            return rag_service_pb2.CreateContextResponse(
                user_id=request.user_id,
                context_id=None,
            )

        return rag_service_pb2.CreateContextResponse(
            user_id=request.user_id,
            context_id=chat_context_id,
        )

    def FlushChatHistory(self, request, context):
        """Flush chat history to database"""
        # Get cache from LRU cache and write back to database
        cache_result = self.lru_cache.get_cache(request.user_id, request.context_id)
        if cache_result:
            self.write_back_task((request.user_id, request.context_id), cache_result)
            self.lru_cache.delete_cache(request.user_id, request.context_id)
            return rag_service_pb2.FlushContextResponse(
                status=code.SUCCESS_STATUS,
            )
        else:
            return rag_service_pb2.FlushContextResponse(
                status=code.FAILURE_STATUS,
            )

    def prepare_prompt(self, request):
        request_embedding = self.embedding_model.encode(request.message)[0]

        # If current web search is enabled, search the web for the user's question
        document_text = None
        if self.bing_search:
            # TODO: search from TiDB when similar document is found
            search_result = self.bing_search.search(request.message)
            if search_result:
                first_link = self.bing_search.get_first_link()
                documents = SimpleWebPageReader(html_to_text=True).load_data(
                    [
                        first_link,
                    ]
                )
                for document in documents:
                    document_text += document.text

            if document_text:
                document_text_embedding = self.embedding_model.encode(document_text)[0]
                try:
                    with Session(self.engine) as session:
                        session.add(
                            web_agent_history.WebAgentHistory(
                                content=document_text,
                                content_embedding=document_text_embedding,
                            )
                        )
                        session.commit()
                except Exception as e:
                    logger.error(f"Failed to save web agent history: {e}")

        # Get user context from database with Range TTL
        user_context = []
        try:
            with Session(self.engine) as session:
                distance = (
                    session_history.SessionHistory.question_embedding.cosine_distance(
                        request_embedding
                    ).label("distance")
                )
                result = (
                    session.query(session_history.SessionHistory, distance)
                    .filter(distance < 0.1)
                    .order_by(distance)
                    .limit(3)
                    .all()
                )

                logger.info(f"User context: {result}")
                for history, distance in result:
                    user_context.append(history)
        except Exception as e:
            logger.error(f"Failed to get user context: {e}")

        # Replace current prompt with context
        prompt = DIET_SYSTEM_PROMPT
        if request.addition:
            prompt = prompt.replace("REFRIGERATOR_CONTENTS", request.addition)
        if document_text:
            prompt = prompt.replace("WEB_SEARCH_RESULT", document_text)
        if user_context:
            # convert user_context list to string
            user_context = "\n".join(
                [f"{history.question}\n{history.answer}" for history in user_context]
            )

            prompt = prompt.replace("PREFERS_LIGHT_MEALS", user_context)

        return prompt

    def write_back_task(self, old_key, old_value):
        """Write back task to database if current LRU cache is full"""
        user_id, context_id = old_key
        # chat_history list, item is (question, question_embedding, answer, answer_embedding, created_at) = old_value
        chat_history = old_value
        logger.info(f"Writing back task to database: {user_id}, {context_id}")

        # Convert to chat histories for batch add
        chat_histories = []
        for (
            question,
            question_embedding,
            answer,
            answer_embedding,
            created_at,
        ) in chat_history:
            chat_histories.append(
                session_history.SessionHistory(
                    session_id=context_id,
                    question=question,
                    question_embedding=question_embedding,
                    answer=answer,
                    answer_embedding=answer_embedding,
                    created_at=created_at,
                )
            )
            logger.info(f"Chat history: {question}, {answer}, {created_at}")

        # Write back all chat history to database
        try:
            with Session(self.engine) as session:
                # session.add_all(chat_histories)
                for chat_history in chat_histories:
                    session.add(chat_history)
                session.commit()
                logger.info(
                    f"Write back task to database: {user_id}, {context_id} successfully"
                )
        except Exception as e:
            session.rollback()
            logger.error(f"Failed to write back task to database: {e}")
