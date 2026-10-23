package com.nutrix.ui.screens

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
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Tune
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
import com.nutrix.model.SensorCalibrationPoint
import com.nutrix.model.SensorCalibrationState
import com.nutrix.ui.components.DemoModeBadge
import com.nutrix.ui.components.PrototypeDisclaimerBanner
import com.nutrix.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CalibrationScreen(
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val calibrationState by SampleData.calibrationState.collectAsState()
  val scope = rememberCoroutineScope()
  var isCalibratingNow by remember { mutableStateOf(false) }
  var calibrationSuccessMessage by remember { mutableStateOf<String?>(null) }

  Surface(
    modifier = modifier
      .fillMaxSize()
      .testTag("sensor_calibration_screen"),
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
            text = "Sensor Calibration",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            color = SoilForestGreen
          )
          Text(
            text = "Smartphone-Based ISE 2-Point Calibration",
            style = MaterialTheme.typography.bodySmall,
            color = SoilTextMuted
          )
        }
        DemoModeBadge()
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Scientific Explanation Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoilGreenLight),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoilGreenMint)
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.Top
        ) {
          Icon(
            imageVector = Icons.Outlined.Tune,
            contentDescription = null,
            tint = SoilForestGreen,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "Calibration improves consistency between sensor readings and compensates for sensor-to-sensor variation, cellulose capillary drift, and temperature effects.",
            style = MaterialTheme.typography.bodySmall,
            color = SoilForestGreen,
            lineHeight = 18.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // POTASSIUM SENSOR CALIBRATION SECTION
      SensorCalibrationSection(
        title = "POTASSIUM (K⁺) SENSOR",
        chemicalFormula = "K⁺",
        point1Label = "Calibration Point 1",
        point1Standard = "1 mM Standard",
        point1Mv = calibrationState.potassiumPoint1.measuredMv,
        point2Label = "Calibration Point 2",
        point2Standard = "10 mM Standard",
        point2Mv = calibrationState.potassiumPoint2.measuredMv,
        calculatedSlope = "${calibrationState.slopeKmVDilution} mV / decade (Nernstian)",
        accentColor = SoilPotassiumColor,
        lightColor = SoilPotassiumLight,
        onRecordPoint = { pointIndex ->
          calibrationSuccessMessage = "Recorded K⁺ Point $pointIndex standard voltage"
        }
      )

      Spacer(modifier = Modifier.height(18.dp))

      // NITRATE SENSOR CALIBRATION SECTION
      SensorCalibrationSection(
        title = "NITRATE (NO₃⁻) SENSOR",
        chemicalFormula = "NO₃⁻",
        point1Label = "Calibration Point 1",
        point1Standard = "0.1 mM Standard",
        point1Mv = calibrationState.nitratePoint1.measuredMv,
        point2Label = "Calibration Point 2",
        point2Standard = "1.0 mM Standard",
        point2Mv = calibrationState.nitratePoint2.measuredMv,
        calculatedSlope = "${calibrationState.slopeNo3mVDilution} mV / decade (Nernstian)",
        accentColor = SoilNitrateColor,
        lightColor = SoilNitrateLight,
        onRecordPoint = { pointIndex ->
          calibrationSuccessMessage = "Recorded NO₃⁻ Point $pointIndex standard voltage"
        }
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Calibration Successful Banner
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SoilCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoilGreenMint)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Outlined.CheckCircle,
              contentDescription = null,
              tint = SoilForestGreen,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "✓ Calibration Successful",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SoilForestGreen
              )
              Text(
                text = "Last calibrated: ${calibrationState.lastCalibratedDate}",
                style = MaterialTheme.typography.bodySmall,
                color = SoilTextMuted
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Action Buttons: [Recalibrate] and [Reset Calibration]
      Button(
        onClick = {
          scope.launch {
            isCalibratingNow = true
            delay(1200)
            SampleData.resetCalibration()
            isCalibratingNow = false
            calibrationSuccessMessage = "Sensors successfully calibrated with standard curves!"
          }
        },
        enabled = !isCalibratingNow,
        colors = ButtonDefaults.buttonColors(containerColor = SoilForestGreen),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .testTag("recalibrate_sensor_btn")
      ) {
        if (isCalibratingNow) {
          CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            color = Color.White,
            strokeWidth = 2.dp
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text("Computing Potentiometric Slopes...")
        } else {
          Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Recalibrate All Sensors", fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedButton(
        onClick = {
          SampleData.resetCalibration()
          calibrationSuccessMessage = "Calibration reset to factory defaults"
        },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = SoilForestGreen),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, SoilForestGreen),
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("reset_calibration_btn")
      ) {
        Icon(imageVector = Icons.Default.Restore, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Reset Calibration", fontWeight = FontWeight.SemiBold)
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Snack/Feedback banner if active
      calibrationSuccessMessage?.let { msg ->
        Surface(
          color = SoilGreenLight,
          shape = RoundedCornerShape(10.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, SoilEmerald)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = SoilEmerald, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = msg, style = MaterialTheme.typography.bodySmall, color = SoilForestGreen, fontWeight = FontWeight.SemiBold)
          }
        }
        Spacer(modifier = Modifier.height(10.dp))
      }

      PrototypeDisclaimerBanner()
    }
  }
}

@Composable
fun SensorCalibrationSection(
  title: String,
  chemicalFormula: String,
  point1Label: String,
  point1Standard: String,
  point1Mv: Double,
  point2Label: String,
  point2Standard: String,
  point2Mv: Double,
  calculatedSlope: String,
  accentColor: Color,
  lightColor: Color,
  onRecordPoint: (Int) -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = SoilCardBg),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorder)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(lightColor),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = chemicalFormula,
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Black,
              color = accentColor
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = SoilTextDark
          )
        }
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = SoilBorder
      )

      // Point 1
      CalibrationPointRow(
        pointTitle = point1Label,
        standardSolution = point1Standard,
        measuredMv = "$point1Mv mV",
        onRecord = { onRecordPoint(1) }
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Point 2
      CalibrationPointRow(
        pointTitle = point2Label,
        standardSolution = point2Standard,
        measuredMv = "$point2Mv mV",
        onRecord = { onRecordPoint(2) }
      )

      Spacer(modifier = Modifier.height(10.dp))

      Surface(
        color = SoilGreenSurface,
        shape = RoundedCornerShape(10.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Sensitivity Slope:", style = MaterialTheme.typography.labelSmall, color = SoilTextMuted)
          Text(text = calculatedSlope, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = SoilForestGreen)
        }
      }
    }
  }
}

@Composable
fun CalibrationPointRow(
  pointTitle: String,
  standardSolution: String,
  measuredMv: String,
  onRecord: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(SoilSurfaceCream)
      .border(1.dp, SoilBorder, RoundedCornerShape(12.dp))
      .padding(12.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(text = pointTitle, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = SoilForestGreen)
      Text(text = standardSolution, style = MaterialTheme.typography.bodySmall, color = SoilTextMuted)
      Text(text = "Measured: $measuredMv", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = SoilTextDark)
    }

    FilledTonalButton(
      onClick = onRecord,
      shape = RoundedCornerShape(10.dp),
      colors = ButtonDefaults.filledTonalButtonColors(
        containerColor = SoilGreenLight,
        contentColor = SoilForestGreen
      )
    ) {
      Text("Record", fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
  }
}
