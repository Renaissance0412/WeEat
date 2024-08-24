from sqlalchemy import Column, Integer, String, Text
from tidb_vector.sqlalchemy import VectorType

from models import Base


class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True, autoincrement=True)
    uuid = Column(String(36), unique=True, nullable=False)
    username = Column(String(50), nullable=False)
    # Bind with the user's device
    token = Column(String(255), nullable=False)
    interests = Column(Text, nullable=True)
    interests_embedding = Column(
        VectorType(768), nullable=True, comment="hnsw(distance=cosine)"
    )
