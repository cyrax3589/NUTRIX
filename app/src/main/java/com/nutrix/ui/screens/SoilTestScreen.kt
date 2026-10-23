package com.nutrix.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nutrix.data.SampleData
import com.nutrix.model.SoilTestRecord
import com.nutrix.ui.components.CloudTelemetryCard
import com.nutrix.ui.components.DemoModeBadge
import com.nutrix.ui.components.NutrixLogo
import com.nutrix.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SoilTestScreen(
  onTestComplete: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isAnalyzing by remember { mutableStateOf(false) }
  var progress by remember { mutableStateOf(0f) }
  var analysisPhase by remember { mutableStateOf("Initializing Capillary Sampling...") }
  var selectedField by remember { mutableStateOf("Demo Field") }
  val cloudReadingState by SampleData.cloudRepository.readingState.collectAsState()
  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(Unit) {
    SampleData.cloudRepository.fetchLatestReading()
  }

  LaunchedEffect(isAnalyzing) {
    if (isAnalyzing) {
      progress = 0f
      analysisPhase = "Capillary Wicking on Cellulose Substrate..."
      delay(800)
      progress = 0.30f
      analysisPhase = "Measuring ISE Potentiometric Voltages..."
      delay(900)
      progress = 0.65f
      analysisPhase = "Stabilizing K⁺ & NO₃⁻ Ion Readings..."
      delay(900)
      progress = 0.90f
      analysisPhase = "Computing Soil Fertility Index..."
      delay(700)
      progress = 1.0f

      // Update sample data with a fresh simulated record
      SampleData.updateLatestTest(
        SoilTestRecord(
          id = "TEST-2026-0902-NEW",
          date = "02 Sep 2026",
          time = "Just now",
          fieldName = selectedField,
          potassiumMgKg = 62.0,
          nitrateMm = 0.32,
          healthScore = 8.2,
          status = "Good",
          readingStatus = "Stable",
          calibrationStatus = "Calibrated",
          phosphorusMgKg = 24.0,
          nitrogenMgKg = 210.0
        )
      )
      delay(400)
      onTestComplete()
    }
  }

  Surface(
    modifier = modifier
      .fillMaxSize()
      .testTag("soil_test_screen"),
    color = SoilSurfaceCream
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          NutrixLogo(size = 42.dp, elevation = 2.dp)
          Column {
            Text(
              text = "New Soil Test",
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.Black,
              color = SoilForestGreen
            )
            Text(
              text = "Guided ISE Sampling Workflow",
              style = MaterialTheme.typography.bodySmall,
              color = SoilTextMuted
            )
          }
        }
        DemoModeBadge()
      }

      Spacer(modifier = Modifier.height(18.dp))

      if (!isAnalyzing) {
        // STEP 1 CARD
        GuidedStepCard(
          stepNumber = 1,
          title = "Prepare Soil Sample",
          description = "Place 2–3 drops of the prepared soil suspension onto the sensor's cellulose sampling area.",
          icon = Icons.Outlined.Science,
          badgeText = "Capillary Wicking"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // STEP 2 CARD
        GuidedStepCard(
          stepNumber = 2,
          title = "Connect Sensor",
          description = "Ensure NUTRIX ISE portable probe is turned on and paired via Bluetooth Low Energy.",
          icon = Icons.Outlined.Sensors,
          badgeText = "Demo Device Connected (ISE-V1)"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // STEP 3 CARD
        GuidedStepCard(
          stepNumber = 3,
          title = "Field Selection & Analyze",
          description = "Select your target field zone to log nutrient readings accurately.",
          icon = Icons.Default.Agriculture,
          badgeText = "Ready to Scan"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Field Selector Chips
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("Demo Field", "North Field", "South Field").forEach { field ->
            FilterChip(
              selected = selectedField == field,
              onClick = { selectedField = field },
              label = { Text(field) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = SoilGreenLight,
                selectedLabelColor = SoilForestGreen
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Verified ESP32 Cloud Sensor Status
        CloudTelemetryCard(
          uiState = cloudReadingState,
          onRefresh = {
            coroutineScope.launch {
              SampleData.cloudRepository.fetchLatestReading()
            }
          }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Big Start Soil Test Button
        Button(
          onClick = { isAnalyzing = true },
          colors = ButtonDefaults.buttonColors(containerColor = SoilForestGreen),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .testTag("start_soil_test_button")
        ) {
          Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Start Soil Test",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      } else {
        // LIVE SIMULATED ANALYSIS ANIMATION CARD
        Spacer(modifier = Modifier.height(20.dp))

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("analyzing_progress_card"),
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = SoilCardBg),
          border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorder)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "Analyzing Soil...",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Black,
              color = SoilForestGreen
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = "Stabilizing sensor readings...",
              style = MaterialTheme.typography.bodyMedium,
              color = SoilTextMuted
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Animated Circular Gauge
            Box(
              modifier = Modifier.size(160.dp),
              contentAlignment = Alignment.Center
            ) {
              CircularProgressIndicator(
                progress = { progress },
                strokeWidth = 10.dp,
                color = SoilForestGreen,
                trackColor = SoilGreenLight,
                strokeCap = StrokeCap.Round,
                modifier = Modifier.fillMaxSize()
              )

              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                  text = "${(progress * 100).toInt()}%",
                  style = MaterialTheme.typography.headlineMedium,
                  fontWeight = FontWeight.Black,
                  color = SoilForestGreen
                )
                Text(
                  text = "Sampling",
                  style = MaterialTheme.typography.labelSmall,
                  color = SoilTextMuted
                )
              }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Current Phase Text with pulse
            Surface(
              color = SoilGreenSurface,
              shape = RoundedCornerShape(12.dp)
            ) {
              Text(
                text = analysisPhase,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = SoilEmerald,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
              )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Simulated Live Sensor Telemetry
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceAround
            ) {
              LiveSensorMiniMetric(label = "Potassium ISE", value = if (progress > 0.4f) "62 mg/kg" else "Stabilizing...")
              LiveSensorMiniMetric(label = "Nitrate ISE", value = if (progress > 0.6f) "0.32 mM" else "Stabilizing...")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Skip / Finish Demo Button for evaluators
            OutlinedButton(
              onClick = {
                SampleData.updateLatestTest(
                  SoilTestRecord(
                    id = "TEST-2026-0902-NEW",
                    date = "02 Sep 2026",
                    time = "Just now",
                    fieldName = selectedField,
                    potassiumMgKg = 62.0,
                    nitrateMm = 0.32,
                    healthScore = 8.2,
                    status = "Good",
                    readingStatus = "Stable",
                    calibrationStatus = "Calibrated",
                    phosphorusMgKg = 24.0,
                    nitrogenMgKg = 210.0
                  )
                )
                onTestComplete()
              },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.testTag("skip_demo_button")
            ) {
              Text("Skip / View Results Immediately")
            }
          }
        }
      }
    }
  }
}

@Composable
fun GuidedStepCard(
  stepNumber: Int,
  title: String,
  description: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  badgeText: String,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = SoilCardBg),
    border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorder)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.Top
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(SoilGreenLight),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "$stepNumber",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Black,
          color = SoilForestGreen
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = formatNutrixText(title),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = SoilTextDark
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = formatNutrixText(description),
          style = MaterialTheme.typography.bodySmall,
          color = SoilTextMuted,
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
          color = SoilGreenSurface,
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "● $badgeText",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = SoilForestGreen,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            fontSize = 11.sp
          )
        }
      }
    }
  }
}

@Composable
fun LiveSensorMiniMetric(label: String, value: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = label, style = MaterialTheme.typography.labelSmall, color = SoilTextMuted)
    Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = SoilForestGreen)
  }
}
