package com.nutrix.data.cloud

import retrofit2.Response
import retrofit2.http.GET

interface NutrixApiService {

  /**
   * Health and MongoDB connection status.
   * GET /health -> {"api":"ok","database":"connected"}
   */
  @GET("health")
  suspend fun getHealth(): Response<CloudHealthResponse>

  /**
   * Latest sensor reading ingested by ESP32 into FastAPI/MongoDB Atlas.
   * GET /api/v1/readings/latest
   */
  @GET("api/v1/readings/latest")
  suspend fun getLatestReading(): Response<CloudSensorReading>
}
