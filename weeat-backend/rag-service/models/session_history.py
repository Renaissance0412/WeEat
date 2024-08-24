from sqlalchemy import Column, Integer, String, Text, ForeignKey, TIMESTAMP
from tidb_vector.sqlalchemy import VectorType

from models import Base

# Ref: https://docs.pingcap.com/tidb/stable/time-to-live
TIME_TO_LIVE = 60 * 60 * 24 * 30  # 1 month


class SessionHistory(Base):
    __tablename__ = "session_history"
    __table_args__ = {
        # Old records will be automatically deleted after 1 month by TiDB
        # We only want to keep the most recent records
        "mysql_TTL": f"created_at + INTERVAL {TIME_TO_LIVE} SECOND",
    }

    id = Column(Integer, primary_key=True, autoincrement=True)
    session_id = Column(String(36), nullable=False)
    question = Column(Text, nullable=False)
    question_embedding = Column(
        VectorType(768), nullable=True, comment="hnsw(distance=cosine)"
    )
    answer = Column(Text, nullable=False)
    answer_embedding = Column(
        VectorType(768), nullable=True, comment="hnsw(distance=cosine)"
    )
    created_at = Column(TIMESTAMP, nullable=False)
