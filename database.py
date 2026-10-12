import os

from pymongo import MongoClient
from pymongo.server_api import ServerApi


MONGO_URI = os.getenv("MONGODB_URI")

if not MONGO_URI:
    raise RuntimeError(
        "MONGODB_URI environment variable is not configured."
    )

client = MongoClient(
    MONGO_URI,
    serverSelectionTimeoutMS=10000,
    server_api=ServerApi("1"),
    tz_aware=True,
)

db = client["NUTRIX"]

readings_collection = db["readings"]
devices_collection = db["devices"]
recommendations_collection = db["recommendations"]


def check_database():
    """Verify that MongoDB Atlas is reachable."""
    client.admin.command("ping")
    return True