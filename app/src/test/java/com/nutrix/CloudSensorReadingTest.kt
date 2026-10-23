package com.nutrix

import com.nutrix.data.cloud.CloudReadingUiState
import com.nutrix.data.cloud.CloudSensorReading
import com.nutrix.data.cloud.NutrixApiService
import com.nutrix.data.cloud.NutrixCloudRepository
import com.nutrix.data.cloud.NutrixNetworkClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response

class CloudSensorReadingTest {

  private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

  private val adapter = moshi.adapter(CloudSensorReading::class.java)

  @Test
  fun testDeserialization_withActualFastApiResponse() {
    val json = """
      {
        "_id": "6acceff3cc78ca0bb350e244",
        "device_id": "NUTRIX_001",
        "sensor": "soil_moisture",
        "raw": 3120,
        "unit": "adc",
        "moisture_percent": 0.0,
        "temperature_c": 29.03,
        "pressure_hpa": 924.32,
        "timestamp": "2026-10-12T14:34:24+00:00"
      }
    """.trimIndent()

    val reading = adapter.fromJson(json)
    assertNotNull(reading)
    assertEquals("6acceff3cc78ca0bb350e244", reading!!.id)
    assertEquals("NUTRIX_001", reading.deviceId)
    assertEquals("soil_moisture", reading.sensor)
    assertEquals(3120, reading.raw)
    assertEquals("adc", reading.unit)
    assertEquals(0.0, reading.moisturePercent!!, 0.001)
    assertEquals(29.03, reading.temperatureC!!, 0.001)
    assertEquals(924.32, reading.pressureHpa!!, 0.001)
    assertEquals("2026-10-12T14:34:24+00:00", reading.timestamp)
    assertFalse(reading.isEmptyState)
  }

  @Test
  fun testDeserialization_withNullOptionalFields() {
    val json = """
      {
        "_id": "6acceff3cc78ca0bb350e244",
        "device_id": "NUTRIX_001",
        "sensor": "soil_moisture",
        "raw": 2840,
        "unit": "adc",
        "moisture_percent": 18.5
      }
    """.trimIndent()

    val reading = adapter.fromJson(json)
    assertNotNull(reading)
    assertNull(reading!!.temperatureC)
    assertNull(reading.pressureHpa)
    assertNull(reading.timestamp)
    assertEquals(18.5, reading.moisturePercent!!, 0.001)
    assertFalse(reading.isEmptyState)
  }

  @Test
  fun testDeserialization_emptyMessageState() {
    val json = """
      {
        "message": "No readings available yet"
      }
    """.trimIndent()

    val reading = adapter.fromJson(json)
    assertNotNull(reading)
    assertEquals("No readings available yet", reading!!.message)
    assertTrue(reading.isEmptyState)
  }

  @Test
  fun testRepository_liveIntegrationWithRenderFastApi() = runBlocking {
    val repository = NutrixCloudRepository()
    val healthResult = repository.checkHealth()
    assertTrue("Health check should succeed against Render", healthResult.isSuccess)
    assertEquals("ok", healthResult.getOrNull()?.api)
    assertEquals("connected", healthResult.getOrNull()?.database)

    val readingState = repository.fetchLatestReading()
    assertTrue("Should be Success or Empty", readingState is CloudReadingUiState.Success || readingState is CloudReadingUiState.Empty)
    if (readingState is CloudReadingUiState.Success) {
      assertNotNull(readingState.reading.deviceId)
      assertNotNull(readingState.reading.moisturePercent)
    }
  }
}
