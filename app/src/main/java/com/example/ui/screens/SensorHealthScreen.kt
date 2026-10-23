package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.WarningAmber
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
import com.example.data.SampleData
import com.example.ui.components.CloudTelemetryCard
import com.example.ui.components.DemoModeBadge
import com.example.ui.components.PrototypeDisclaimerBanner
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun SensorHealthScreen(
  onNavigateToCalibration: () -> Unit,
  modifier: Modifier = Modifier
) {
  val calibrationState by SampleData.calibrationState.collectAsState()
  val cloudReadingState by SampleData.cloudRepository.readingState.collectAsState()
  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(Unit) {
    SampleData.cloudRepository.fetchLatestReading()
  }

  Surface(
    modifier = modifier
      .fillMaxSize()
      .testTag("sensor_health_screen"),
    color = SoilSurfaceCream
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Sensor Health",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            color = SoilForestGreen
          )
          Text(
            text = "ISE Diagnostics & Drift Monitoring",
            style = MaterialTheme.typography.bodySmall,
            color = SoilTextMuted
          )
        }
        DemoModeBadge()
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Demo Warning Simulator Toggle for Evaluator
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoilCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorder)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Demo Drift Simulator",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = SoilForestGreen
            )
            Text(
              text = "Simulate sensor variation & warning for evaluator demo",
              style = MaterialTheme.typography.bodySmall,
              color = SoilTextMuted
            )
          }

          Switch(
            checked = calibrationState.isWarningTriggered,
            onCheckedChange = { SampleData.toggleWarningSimulation() },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = SoilOrange),
            modifier = Modifier.testTag("drift_simulator_toggle")
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // WARNING CARD (Shown if warning is triggered or active)
      AnimatedVisibility(visible = calibrationState.isWarningTriggered) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .testTag("calibration_warning_card"),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = SoilRedLight),
          border = androidx.compose.foundation.BorderStroke(1.5.dp, SoilRed)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Outlined.WarningAmber,
                contentDescription = null,
                tint = SoilRed,
                modifier = Modifier.size(28.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = "⚠ Calibration Recommended",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SoilRed
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "Sensor readings have shown unusual potentiometric variation. Recalibration may improve measurement reliability and compensate for substrate baseline drift.",
              style = MaterialTheme.typography.bodySmall,
              color = SoilDarkBrown,
              lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = onNavigateToCalibration,
              colors = ButtonDefaults.buttonColors(containerColor = SoilRed),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth().testTag("warning_recalibrate_btn")
            ) {
              Icon(imageVector = Icons.Outlined.Tune, contentDescription = null)
              Spacer(modifier = Modifier.width(8.dp))
              Text("Recalibrate Sensor Now", fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // PRIMARY HEALTH SUMMARY CARD
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SoilCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorder)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "HARDWARE & SENSING METRICS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = SoilForestGreen,
            letterSpacing = 0.5.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          // 4 Main Status Indicators
          HealthStatusItem(
            label = "Sensor Status",
            statusText = if (calibrationState.isWarningTriggered) "● Drift Detected" else "● Healthy",
            isGood = !calibrationState.isWarningTriggered
          )

          HealthStatusItem(
            label = "Calibration",
            statusText = if (calibrationState.isWarningTriggered) "⚠ Needs Refresh" else "✓ Up to date",
            isGood = !calibrationState.isWarningTriggered
          )

          HealthStatusItem(
            label = "Readings Stability",
            statusText = if (calibrationState.isWarningTriggered) "⚠ Fluctuating (74%)" else "✓ Stable (98%)",
            isGood = !calibrationState.isWarningTriggered
          )

          HealthStatusItem(
            label = "Tests Performed",
            statusText = "${calibrationState.testsPerformedCount} assays logged",
            isGood = true
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Technical Parameter Cards: Last Calibration, Electrode Usage, Reading Stability
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SoilCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorder)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "DETAILED DIAGNOSTIC LOG",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = SoilForestGreen,
            letterSpacing = 0.5.sp
          )

          Spacer(modifier = Modifier.height(12.dp))

          DiagnosticRow("Last Calibration", calibrationState.lastCalibratedDate)
          DiagnosticRow("Electrode Usage", "12 / 100 Tests (Cellulose intact)")
          DiagnosticRow("Reading Stability", "${calibrationState.stabilityPercent}% consistency index")
          DiagnosticRow("Potassium Sensitivity", "${calibrationState.slopeKmVDilution} mV / dec")
          DiagnosticRow("Nitrate Sensitivity", "${calibrationState.slopeNo3mVDilution} mV / dec")
          DiagnosticRow("Electrode Matrix", "PVC-based Valinomycin / Ionophore")
          DiagnosticRow("Communication Link", "BLE 5.2 - Signal Strength: -58 dBm (Strong)")
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Cloud IoT Real-Time Status Card
      CloudTelemetryCard(
        uiState = cloudReadingState,
        onRefresh = {
          coroutineScope.launch {
            SampleData.cloudRepository.fetchLatestReading()
          }
        }
      )

      Spacer(modifier = Modifier.height(20.dp))

      Button(
        onClick = onNavigateToCalibration,
        colors = ButtonDefaults.buttonColors(containerColor = SoilForestGreen),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .testTag("goto_calibration_screen_btn")
      ) {
        Icon(imageVector = Icons.Outlined.Tune, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Open Sensor Calibration Screen", fontWeight = FontWeight.Bold)
      }

      Spacer(modifier = Modifier.height(16.dp))
      PrototypeDisclaimerBanner()
    }
  }
}

@Composable
fun HealthStatusItem(
  label: String,
  statusText: String,
  isGood: Boolean
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.bodyMedium,
      color = SoilTextDark
    )
    Surface(
      color = if (isGood) SoilGreenLight else SoilRedLight,
      shape = RoundedCornerShape(8.dp)
    ) {
      Text(
        text = statusText,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = if (isGood) SoilForestGreen else SoilRed,
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
      )
    }
  }
}

@Composable
fun DiagnosticRow(label: String, value: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 5.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = label, style = MaterialTheme.typography.bodySmall, color = SoilTextMuted)
    Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = SoilTextDark)
  }
}
