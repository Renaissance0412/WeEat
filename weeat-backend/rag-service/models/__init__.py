import os
import sys

from sqlalchemy.orm import declarative_base, Session

sys.path.append(os.path.dirname(os.path.abspath(__file__)))

# Global models base
Base = declarative_base()
