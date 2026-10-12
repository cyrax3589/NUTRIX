from pymongo import MongoClient
from pymongo.server_api import ServerApi

MONGO_URI = "mongodb://127.0.0.1:27017"

client = MongoClient(
    MONGO_URI,
    serverSelectionTimeoutMS=3000,
    server_api=ServerApi("1")
)

db = client["NUTRIX"]

readings_collection = db["readings"]
devices_collection = db["devices"]
recommendations_collection = db["recommendations"]


def check_database():
    """Verify that the local MongoDB server is reachable."""
    client.admin.command("ping")
    return True