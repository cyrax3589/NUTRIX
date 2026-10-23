package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.cloud.CloudReadingUiState
import com.example.data.cloud.CloudSensorReading
import com.example.ui.theme.*

/**
 * Cloud IoT Live Telemetry Card
 * Displays the verified live ESP32 soil moisture, BMP280 temperature, and
 * BMP280 atmospheric pressure streamed via FastAPI on Render and stored in MongoDB Atlas.
 */
@Composable
fun CloudTelemetryCard(
  uiState: CloudReadingUiState,
  onRefresh: () -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val rotation by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "spin"
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("cloud_telemetry_card"),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = SoilCardBg),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorderLight)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      // Header: Title, Live indicator, and Refresh button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(SoilGreenLight),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.CloudSync,
              contentDescription = "Cloud IoT",
              tint = SoilEmerald,
              modifier = Modifier.size(20.dp)
            )
          }

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "ESP32 CLOUD TELEMETRY",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = SoilTextMuted,
                letterSpacing = 1.sp,
                fontSize = 10.sp
              )
              Spacer(modifier = Modifier.width(6.dp))
              // Live status chip
              val (dotColor, statusText) = when (uiState) {
                is CloudReadingUiState.Success -> {
                  if (uiState.isStale) SoilAmber to "STALE" else SoilEmerald to "LIVE"
                }
                is CloudReadingUiState.Loading -> SoilAmber to "CONNECTING"
                is CloudReadingUiState.Error -> SoilRed to "OFFLINE"
                is CloudReadingUiState.Empty -> SoilAmber to "NO DATA"
                CloudReadingUiState.Idle -> SoilTextMuted to "IDLE"
              }

              Surface(
                color = dotColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Box(
                    modifier = Modifier
                      .size(6.dp)
                      .clip(CircleShape)
                      .background(dotColor)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = statusText,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = dotColor
                  )
                }
              }
            }

            Text(
              text = "Render FastAPI • MongoDB Atlas",
              style = MaterialTheme.typography.bodySmall,
              fontSize = 11.sp,
              color = SoilTextDark,
              fontWeight = FontWeight.Medium
            )
          }
        }

        // Manual Refresh Button
        IconButton(
          onClick = onRefresh,
          modifier = Modifier
            .size(36.dp)
            .testTag("refresh_cloud_reading_btn")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Refresh Cloud Readings",
            tint = SoilEmerald,
            modifier = Modifier
              .size(20.dp)
              .then(if (uiState is CloudReadingUiState.Loading) Modifier.rotate(rotation) else Modifier)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Content based on UI State
      when (uiState) {
        is CloudReadingUiState.Loading -> {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(110.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                color = SoilEmerald,
                strokeWidth = 3.dp
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "Contacting Render backend...",
                style = MaterialTheme.typography.bodySmall,
                color = SoilTextMuted
              )
            }
          }
        }

        is CloudReadingUiState.Success -> {
          CloudReadingContent(
            reading = uiState.reading,
            fetchedAt = uiState.fetchedAtTime,
            isStale = uiState.isStale
          )
        }

        is CloudReadingUiState.Error -> {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .background(SoilRedLight, RoundedCornerShape(12.dp))
              .border(1.dp, SoilRed.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
              .padding(12.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = SoilRed,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (uiState.isColdStarting) "Backend Initializing" else "Cloud Sync Failed",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SoilRed
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = uiState.errorMessage,
              style = MaterialTheme.typography.bodySmall,
              color = SoilDarkBrown,
              fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
              onClick = onRefresh,
              colors = ButtonDefaults.buttonColors(containerColor = SoilRed),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
              modifier = Modifier.height(32.dp)
            ) {
              Text("Retry Connection", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            if (uiState.cachedReading != null) {
              Spacer(modifier = Modifier.height(10.dp))
              HorizontalDivider(color = SoilRed.copy(alpha = 0.2f))
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "Last Cached Telemetry:",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = SoilTextMuted
              )
              CloudReadingContent(reading = uiState.cachedReading, fetchedAt = "Cached", isStale = true)
            }
          }
        }

        is CloudReadingUiState.Empty -> {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = uiState.message,
              style = MaterialTheme.typography.bodyMedium,
              color = SoilTextMuted
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onRefresh) {
              Text("Check for Readings")
            }
          }
        }

        CloudReadingUiState.Idle -> {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
          ) {
            OutlinedButton(onClick = onRefresh) {
              Text("Connect to Cloud IoT")
            }
          }
        }
      }
    }
  }
}

@Composable
private fun CloudReadingContent(
  reading: CloudSensorReading,
  fetchedAt: String,
  isStale: Boolean
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    // Top device and sensor info
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = "Device: ${reading.deviceId ?: "NUTRIX_001"}",
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = SoilForestGreen
      )
      Text(
        text = "Raw ADC: ${reading.raw ?: "--"} (${reading.unit ?: "adc"})",
        style = MaterialTheme.typography.bodySmall,
        color = SoilTextMuted
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // 3 Main Verified Cloud Metrics: Soil Moisture %, BMP280 Temp, BMP280 Pressure
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // Metric 1: Calibrated Soil Moisture %
      CloudMetricPill(
        icon = Icons.Outlined.WaterDrop,
        iconTint = SoilEmerald,
        iconBg = SoilGreenLight,
        label = "Soil Moisture",
        value = reading.moisturePercent?.let { "%.1f%%".format(it) } ?: "--",
        subtext = "Calibrated %",
        modifier = Modifier.weight(1f)
      )

      // Metric 2: BMP280 Air Temperature °C
      CloudMetricPill(
        icon = Icons.Outlined.DeviceThermostat,
        iconTint = SoilOrange,
        iconBg = SoilAmberLight,
        label = "BMP280 Temp",
        value = reading.temperatureC?.let { "%.1f°C".format(it) } ?: "N/A",
        subtext = "Ambient Air",
        modifier = Modifier.weight(1f)
      )

      // Metric 3: BMP280 Atmospheric Pressure hPa
      CloudMetricPill(
        icon = Icons.Outlined.Compress,
        iconTint = SoilForestGreen,
        iconBg = SoilGreenSurface,
        label = "Atm. Pressure",
        value = reading.pressureHpa?.let { "%.1f".format(it) } ?: "N/A",
        subtext = "hPa Barometric",
        modifier = Modifier.weight(1f)
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Bottom telemetry timestamps & data provenance
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Sensor Time: ${formatCloudTimestamp(reading.timestamp)}",
          style = MaterialTheme.typography.labelSmall,
          fontSize = 10.sp,
          color = SoilTextMuted
        )
        Text(
          text = "App Synced: $fetchedAt",
          style = MaterialTheme.typography.labelSmall,
          fontSize = 10.sp,
          color = SoilTextMuted
        )
      }

      Surface(
        color = SoilSurfaceCream,
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorderLight)
      ) {
        Text(
          text = "ESP32 Verified",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          fontSize = 9.sp,
          color = SoilEmerald,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
      }
    }
  }
}

@Composable
private fun CloudMetricPill(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconTint: Color,
  iconBg: Color,
  label: String,
  value: String,
  subtext: String,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    color = Color.White,
    shape = RoundedCornerShape(16.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorderLight),
    shadowElevation = 1.dp
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.Start
    ) {
      Box(
        modifier = Modifier
          .size(26.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(iconBg),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = label,
          tint = iconTint,
          modifier = Modifier.size(16.dp)
        )
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        fontSize = 9.sp,
        fontWeight = FontWeight.SemiBold,
        color = SoilTextMuted
      )
      Text(
        text = value,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = SoilTextDark,
        fontSize = 13.sp
      )
      Text(
        text = subtext,
        style = MaterialTheme.typography.labelSmall,
        fontSize = 8.5.sp,
        color = SoilTextMuted
      )
    }
  }
}

private fun formatCloudTimestamp(iso: String?): String {
  if (iso.isNullOrBlank()) return "N/A"
  return try {
    // "2026-10-12T14:34:24+00:00" -> extract "14:34:24 UTC"
    val timePart = iso.substringAfter("T").substringBefore("+").substringBefore("Z")
    "$timePart UTC"
  } catch (_: Exception) {
    iso
  }
}
