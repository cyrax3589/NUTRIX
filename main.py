from contextlib import asynccontextmanager
from datetime import datetime, timezone

from bson import ObjectId
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field
from typing import Optional

from backend.database import (
    readings_collection,
    check_database,
)


@asynccontextmanager
async def lifespan(app: FastAPI):
    # Check MongoDB when the API starts.
    check_database()
    yield


app = FastAPI(
    title="NUTRIX API",
    description="Local soil-monitoring backend",
    version="1.1.0",
    lifespan=lifespan,
)


class SoilReading(BaseModel):
    device_id: str = "NUTRIX_001"
    sensor: str = "soil_moisture"
    raw: int = Field(ge=0, le=4095)
    unit: str = "adc"
    moisture_percent: Optional[float] = None

    # BMP280 fields (optional: the ESP32 omits them if the sensor fails).
    temperature_c: Optional[float] = Field(default=None, ge=-40, le=85)
    pressure_hpa: Optional[float] = Field(default=None, ge=300, le=1100)

    timestamp: datetime = Field(
        default_factory=lambda: datetime.now(timezone.utc)
    )


@app.get("/health")
def health():
    try:
        check_database()
        return {
            "api": "ok",
            "database": "connected",
        }
    except Exception:
        raise HTTPException(
            status_code=503,
            detail="Database unavailable",
        )


@app.post("/api/v1/readings", status_code=201)
def create_reading(reading: SoilReading):
    document = reading.model_dump()

    result = readings_collection.insert_one(document)

    return {
        "message": "Reading saved",
        "id": str(result.inserted_id),
        "device_id": reading.device_id,
        "soil_moisture_raw": reading.raw,
        "temperature_c": reading.temperature_c,
        "pressure_hpa": reading.pressure_hpa,
        "timestamp": reading.timestamp.isoformat(),
    }


@app.get("/api/v1/readings/latest")
def get_latest_reading():
    document = readings_collection.find_one(
        sort=[("timestamp", -1), ("_id", -1)]
    )

    if document is None:
        return {"message": "No readings available yet"}

    document["_id"] = str(document["_id"])
    document["timestamp"] = document["timestamp"].isoformat()

    return document