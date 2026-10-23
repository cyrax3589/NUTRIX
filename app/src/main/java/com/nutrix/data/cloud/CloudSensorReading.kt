package com.nutrix.data.cloud

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data model for the latest sensor telemetry returned from FastAPI:
 * GET /api/v1/readings/latest
 *
 * Example JSON:
 * {
 *   "_id": "6acceff3cc78ca0bb350e244",
 *   "device_id": "NUTRIX_001",
 *   "sensor": "soil_moisture",
 *   "raw": 3120,
 *   "unit": "adc",
 *   "moisture_percent": 0.0,
 *   "temperature_c": 29.03,
 *   "pressure_hpa": 924.32,
 *   "timestamp": "2026-10-12T14:34:24+00:00"
 * }
 */
@JsonClass(generateAdapter = true)
data class CloudSensorReading(
  @Json(name = "_id") val id: String? = null,
  @Json(name = "device_id") val deviceId: String? = null,
  @Json(name = "sensor") val sensor: String? = null,
  @Json(name = "raw") val raw: Int? = null,
  @Json(name = "unit") val unit: String? = null,
  @Json(name = "moisture_percent") val moisturePercent: Double? = null,
  @Json(name = "temperature_c") val temperatureC: Double? = null,
  @Json(name = "pressure_hpa") val pressureHpa: Double? = null,
  @Json(name = "timestamp") val timestamp: String? = null,
  @Json(name = "message") val message: String? = null
) {
  /**
   * Helper check to detect whether the response is an empty message state
   * like {"message": "No readings available yet"}
   */
  val isEmptyState: Boolean
    get() = (id == null && deviceId == null && raw == null && message != null) ||
            (id == null && deviceId == null && raw == null && moisturePercent == null)
}

/**
 * Health check response:
 * GET /health -> {"api":"ok","database":"connected"}
 */
@JsonClass(generateAdapter = true)
data class CloudHealthResponse(
  @Json(name = "api") val api: String? = null,
  @Json(name = "database") val database: String? = null
)
