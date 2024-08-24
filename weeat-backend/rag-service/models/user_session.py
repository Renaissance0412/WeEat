from sqlalchemy import Column, Integer, String, ForeignKey

from models import Base


class UserSession(Base):
    __tablename__ = "user_sessions"
    id = Column(Integer, primary_key=True, autoincrement=True)
    user_uuid = Column(String(36), nullable=False)
    session_id = Column(String(36), unique=True, nullable=False)
