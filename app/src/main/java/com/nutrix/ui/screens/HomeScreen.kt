package com.nutrix.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nutrix.data.SampleData
import com.nutrix.ui.components.*
import com.nutrix.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
  onNavigateToTest: () -> Unit,
  onNavigateToRecommendations: () -> Unit,
  onNavigateToCalibration: () -> Unit,
  onNavigateToSensorHealth: () -> Unit,
  modifier: Modifier = Modifier
) {
  val latestTest by SampleData.currentLatestTest.collectAsState()
  val calibrationState by SampleData.calibrationState.collectAsState()
  val profile by SampleData.farmerProfile.collectAsState()
  val cloudReadingState by SampleData.cloudRepository.readingState.collectAsState()
  val coroutineScope = rememberCoroutineScope()

  // Lifecycle-aware periodic polling every 12 seconds while HomeScreen is active
  LaunchedEffect(Unit) {
    // Initial fetch
    SampleData.cloudRepository.fetchLatestReading()
    while (true) {
      kotlinx.coroutines.delay(12000)
      SampleData.cloudRepository.fetchLatestReading()
    }
  }

  Surface(
    modifier = modifier
      .fillMaxSize()
      .testTag("home_screen"),
    color = SoilSurfaceCream
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
      // Top App Title & Demo Badge Row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          NutrixLogo(
            size = 46.dp,
            elevation = 3.dp
          )
          Column {
            Text(
              text = "NUTRIX",
              fontFamily = PhilosopherFontFamily,
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.Bold,
              color = SoilEmerald,
              letterSpacing = 1.sp
            )
            Text(
              text = "AIoT NUTRIENT MONITORING",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.SemiBold,
              color = SoilTextMuted,
              letterSpacing = 1.sp,
              fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Welcome, ${profile.name}",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = SoilForestGreen
            )
          }
        }

        DemoModeBadge()
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Location & Weather Info Card
      FieldWeatherCard(
        location = "Bengaluru, KA",
        temperature = "28°C",
        weatherCondition = "Sunny"
      )

      Spacer(modifier = Modifier.height(14.dp))

      // SOIL HEALTH SCORE Semicircular Gauge
      SemicircularHealthGaugeCard(
        score = latestTest.healthScore,
        maxScore = 10.0,
        status = latestTest.status,
        lastTestDate = latestTest.date,
        fieldName = latestTest.fieldName
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Processed NPK Soil Balance Chart Component
      NpkSoilBalanceChart(
        record = latestTest,
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Verified ESP32 Cloud Telemetry (Render FastAPI + MongoDB Atlas)
      CloudTelemetryCard(
        uiState = cloudReadingState,
        onRefresh = {
          coroutineScope.launch {
            SampleData.cloudRepository.fetchLatestReading()
          }
        }
      )

      Spacer(modifier = Modifier.height(14.dp))

      // High-Density Scan New Sample Card (Primary Action Card)
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable(onClick = onNavigateToTest)
          .testTag("scan_soil_button"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SoilEmerald),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
        ) {
          // Decorative background circle from HTML
          Box(
            modifier = Modifier
              .align(Alignment.BottomEnd)
              .offset(x = 16.dp, y = 16.dp)
              .size(90.dp)
              .clip(CircleShape)
              .background(Color.White.copy(alpha = 0.12f))
          )

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Scan New Sample",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                text = "Start portable sensor analysis",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f)
              )
            }

            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.22f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Scan New Sample",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Latest Recommendation Card (from High Density design)
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable(onClick = onNavigateToRecommendations)
          .testTag("latest_recommendation_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorderLight)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Text(
            text = "LATEST RECOMMENDATION",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = SoilTextMuted,
            letterSpacing = 1.sp,
            fontSize = 10.sp
          )

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(SoilSurfaceCream)
                .border(2.dp, SoilGreenLight, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "🌱", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Rice Crop Suitability",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SoilTextDark
              )
              Text(
                text = "92% match based on Nitrate levels.",
                style = MaterialTheme.typography.bodySmall,
                color = SoilTextMuted,
                fontSize = 11.sp
              )
            }

            Icon(
              imageVector = Icons.Default.ChevronRight,
              contentDescription = "Open Recommendations",
              tint = SoilEmerald,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Quick Sensor Health Banner (clickable shortcut)
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable(onClick = onNavigateToSensorHealth)
          .testTag("home_sensor_status_banner"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (calibrationState.isWarningTriggered) SoilRedLight else SoilGreenLight
        ),
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          if (calibrationState.isWarningTriggered) SoilRed.copy(alpha = 0.5f) else SoilBorderLight
        )
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(if (calibrationState.isWarningTriggered) SoilRed else SoilEmerald),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (calibrationState.isWarningTriggered) Icons.Default.Warning else Icons.Outlined.Sensors,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = if (calibrationState.isWarningTriggered) "Sensor Recalibration Needed" else "Sensor Probe: Healthy & Connected",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (calibrationState.isWarningTriggered) SoilRed else SoilForestGreen,
                fontSize = 13.sp
              )
              Text(
                text = if (calibrationState.isWarningTriggered) "Drift detected • Tap to inspect" else "BLE #4829 • Capillary ISE substrate ready",
                style = MaterialTheme.typography.bodySmall,
                color = SoilTextMuted,
                fontSize = 11.sp
              )
            }
          }
          Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Details",
            tint = SoilForestGreen,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Prototype disclaimer footer
      PrototypeDisclaimerBanner()
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}
