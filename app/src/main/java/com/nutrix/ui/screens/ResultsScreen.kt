package com.nutrix.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Verified
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

@Composable
fun ResultsScreen(
  onNavigateToHistory: () -> Unit,
  onNavigateToRecommendations: () -> Unit,
  modifier: Modifier = Modifier
) {
  val latestTest by SampleData.currentLatestTest.collectAsState()

  Surface(
    modifier = modifier
      .fillMaxSize()
      .testTag("soil_results_screen"),
    color = SoilSurfaceCream
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
      // Header with Demo Badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Soil Analysis Results",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            color = SoilForestGreen
          )
          Text(
            text = "Field ISE Potentiometric Assay",
            style = MaterialTheme.typography.bodySmall,
            color = SoilTextMuted
          )
        }
        DemoModeBadge()
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Overall Soil Health Gauge Card
      SemicircularHealthGaugeCard(
        score = latestTest.healthScore,
        maxScore = 10.0,
        status = latestTest.status,
        lastTestDate = latestTest.date,
        fieldName = latestTest.fieldName
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Processed NPK Soil Balance Chart Component
      NpkSoilBalanceChart(
        record = latestTest,
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Sensor Status & Metadata Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SoilCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorder)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Outlined.Verified,
              contentDescription = null,
              tint = SoilForestGreen,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Sensor Telemetry & Validation",
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold,
              color = SoilForestGreen
            )
          }

          HorizontalDivider(
            modifier = Modifier.padding(vertical = 10.dp),
            color = SoilBorder
          )

          MetadataRow(label = "Sensor Reading Status", value = latestTest.readingStatus, isHighlight = true)
          MetadataRow(label = "Calibration Status", value = latestTest.calibrationStatus, isHighlight = true)
          MetadataRow(label = "Test Date & Time", value = "${latestTest.date}, ${latestTest.time}")
          MetadataRow(label = "Field Zone", value = latestTest.fieldName)
          MetadataRow(label = "Cellulose Substrate", value = "Capillary Wicking OK")
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Two Main Navigation CTA Buttons
      Button(
        onClick = onNavigateToRecommendations,
        colors = ButtonDefaults.buttonColors(containerColor = SoilForestGreen),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp)
          .testTag("get_recommendations_cta_btn")
      ) {
        Icon(imageVector = Icons.Outlined.AutoAwesome, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Get AI Crop & Fertilizer Recommendations",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedButton(
        onClick = onNavigateToHistory,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = SoilForestGreen),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, SoilForestGreen),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("view_detailed_analysis_btn")
      ) {
        Icon(imageVector = Icons.Outlined.History, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "View Detailed History & Trends",
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.SemiBold
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
      PrototypeDisclaimerBanner()
    }
  }
}

@Composable
fun MetadataRow(
  label: String,
  value: String,
  isHighlight: Boolean = false
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall,
      color = SoilTextMuted
    )
    Text(
      text = value,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.Bold,
      color = if (isHighlight) SoilForestGreen else SoilTextDark
    )
  }
}
