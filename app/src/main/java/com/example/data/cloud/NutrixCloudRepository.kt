package com.example.data.cloud

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

sealed class CloudReadingUiState {
  object Idle : CloudReadingUiState()
  object Loading : CloudReadingUiState()
  data class Success(
    val reading: CloudSensorReading,
    val fetchedAtTime: String,
    val isStale: Boolean = false
  ) : CloudReadingUiState()
  data class Empty(
    val message: String,
    val fetchedAtTime: String
  ) : CloudReadingUiState()
  data class Error(
    val errorMessage: String,
    val isColdStarting: Boolean = false,
    val cachedReading: CloudSensorReading? = null
  ) : CloudReadingUiState()
}

class NutrixCloudRepository(
  private val apiService: NutrixApiService = NutrixNetworkClient.apiService
) {

  private val _readingState = MutableStateFlow<CloudReadingUiState>(CloudReadingUiState.Idle)
  val readingState: StateFlow<CloudReadingUiState> = _readingState.asStateFlow()

  private val _backendHealth = MutableStateFlow<CloudHealthResponse?>(null)
  val backendHealth: StateFlow<CloudHealthResponse?> = _backendHealth.asStateFlow()

  private var lastSuccessfulReading: CloudSensorReading? = null

  suspend fun checkHealth(): Result<CloudHealthResponse> {
    return try {
      val response = apiService.getHealth()
      if (response.isSuccessful && response.body() != null) {
        val health = response.body()!!
        _backendHealth.value = health
        Result.success(health)
      } else {
        Result.failure(Exception("Health check returned HTTP ${response.code()}"))
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  suspend fun fetchLatestReading(): CloudReadingUiState {
    if (_readingState.value !is CloudReadingUiState.Success) {
      _readingState.value = CloudReadingUiState.Loading
    }

    val currentTimeStr = formatLocalTime(System.currentTimeMillis())

    return try {
      val response = apiService.getLatestReading()
      if (response.isSuccessful) {
        val body = response.body()
        if (body != null) {
          if (body.isEmptyState) {
            val emptyState = CloudReadingUiState.Empty(
              message = body.message ?: "No sensor readings available yet in database.",
              fetchedAtTime = currentTimeStr
            )
            _readingState.value = emptyState
            emptyState
          } else {
            val isStale = checkIfReadingIsStale(body.timestamp)
            lastSuccessfulReading = body
            val successState = CloudReadingUiState.Success(
              reading = body,
              fetchedAtTime = currentTimeStr,
              isStale = isStale
            )
            _readingState.value = successState
            successState
          }
        } else {
          val emptyState = CloudReadingUiState.Empty(
            message = "No response body received from server.",
            fetchedAtTime = currentTimeStr
          )
          _readingState.value = emptyState
          emptyState
        }
      } else {
        val errorMsg = when (response.code()) {
          404 -> "Reading endpoint not found or database collection empty."
          500, 502, 503, 504 -> "Render server or MongoDB Atlas is starting up (HTTP ${response.code()}). Please retry shortly."
          else -> "Server returned HTTP ${response.code()} (${response.message()})"
        }
        val isColdStart = response.code() in listOf(502, 503, 504)
        val errorState = CloudReadingUiState.Error(
          errorMessage = errorMsg,
          isColdStarting = isColdStart,
          cachedReading = lastSuccessfulReading
        )
        _readingState.value = errorState
        errorState
      }
    } catch (e: IOException) {
      val isTimeout = e.message?.contains("timeout", ignoreCase = true) == true
      val userFriendlyMsg = if (isTimeout) {
        "Render free tier server waking up from sleep. Reconnecting..."
      } else {
        "Network connection offline or unreachable. Please check internet access."
      }
      val errorState = CloudReadingUiState.Error(
        errorMessage = userFriendlyMsg,
        isColdStarting = isTimeout,
        cachedReading = lastSuccessfulReading
      )
      _readingState.value = errorState
      errorState
    } catch (e: Exception) {
      val errorState = CloudReadingUiState.Error(
        errorMessage = "Error: ${e.localizedMessage ?: "Unknown network error"}",
        cachedReading = lastSuccessfulReading
      )
      _readingState.value = errorState
      errorState
    }
  }

  private fun checkIfReadingIsStale(timestampIso: String?): Boolean {
    if (timestampIso.isNullOrBlank()) return false
    return try {
      val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
      }
      val cleaned = timestampIso.substringBefore("+").substringBefore("Z")
      val readingTime = sdf.parse(cleaned)?.time ?: return false
      val ageMillis = System.currentTimeMillis() - readingTime
      // Stale if older than 5 minutes
      ageMillis > (5 * 60 * 1000L)
    } catch (_: Exception) {
      false
    }
  }

  private fun formatLocalTime(millis: Long): String {
    val sdf = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
    return sdf.format(Date(millis))
  }
}
