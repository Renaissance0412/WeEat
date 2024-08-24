from sqlalchemy import Column, Integer, Text
from tidb_vector.sqlalchemy import VectorType

from models import Base


class WebAgentHistory(Base):
    __tablename__ = "web_agent_history"
    id = Column(Integer, primary_key=True, autoincrement=True)
    content = Column(Text, nullable=False)
    content_embedding = Column(
        VectorType(768), nullable=True, comment="hnsw(distance=cosine)"
    )
