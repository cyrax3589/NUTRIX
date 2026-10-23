package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocationOn
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
import androidx.compose.ui.window.Dialog
import com.example.data.SampleData
import com.example.model.SoilTestRecord
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HistoryScreen(
  modifier: Modifier = Modifier
) {
  val historyList by SampleData.historyList.collectAsState()
  var selectedRecordForDetail by remember { mutableStateOf<SoilTestRecord?>(null) }
  var selectedFieldFilter by remember { mutableStateOf("All Fields") }

  val filteredHistory = remember(historyList, selectedFieldFilter) {
    if (selectedFieldFilter == "All Fields") historyList
    else historyList.filter { it.fieldName == selectedFieldFilter }
  }

  Surface(
    modifier = modifier
      .fillMaxSize()
      .testTag("soil_history_screen"),
    color = SoilSurfaceCream
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
      // Header
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            NutrixLogo(size = 44.dp, elevation = 2.dp)
            Column {
              Text(
                text = "Soil History",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = SoilEmerald,
                letterSpacing = (-0.5).sp
              )
              Text(
                text = "HISTORICAL SENSOR LOGS",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = SoilTextMuted,
                letterSpacing = 1.sp,
                fontSize = 10.sp
              )
            }
          }
          DemoModeBadge()
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Interactive Trend Chart
        SoilNutrientTrendChart(history = historyList)

        Spacer(modifier = Modifier.height(14.dp))

        // Filter Chips
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("All Fields", "Demo Field", "North Field", "South Field").forEach { filter ->
            FilterChip(
              selected = selectedFieldFilter == filter,
              onClick = { selectedFieldFilter = filter },
              label = { Text(filter, fontSize = 11.sp, fontWeight = if (selectedFieldFilter == filter) FontWeight.Bold else FontWeight.Medium) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = SoilGreenLight,
                selectedLabelColor = SoilForestGreen,
                containerColor = Color.White
              ),
              border = FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = selectedFieldFilter == filter,
                borderColor = SoilBorderLight,
                selectedBorderColor = SoilEmerald
              ),
              shape = CircleShape
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "PREVIOUS SOIL TESTS (${filteredHistory.size})",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = SoilTextMuted,
          letterSpacing = 1.sp,
          fontSize = 10.sp
        )

        Spacer(modifier = Modifier.height(8.dp))
      }

      // History Test Cards
      items(filteredHistory, key = { it.id }) { testRecord ->
        HistoryTestItemCard(
          record = testRecord,
          onClick = { selectedRecordForDetail = testRecord }
        )
        Spacer(modifier = Modifier.height(10.dp))
      }

      item {
        Spacer(modifier = Modifier.height(8.dp))
        PrototypeDisclaimerBanner()
      }
    }
  }

  // Detail Dialog Modal
  selectedRecordForDetail?.let { record ->
    SoilTestDetailDialog(
      record = record,
      onDismiss = { selectedRecordForDetail = null }
    )
  }
}

@Composable
fun HistoryTestItemCard(
  record: SoilTestRecord,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("history_item_${record.id}"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = SoilCardBg),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorderLight)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Outlined.CalendarMonth,
            contentDescription = null,
            tint = SoilForestGreen,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = record.date,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = SoilTextDark
          )
          Text(
            text = " • ${record.fieldName}",
            style = MaterialTheme.typography.bodySmall,
            color = SoilTextMuted
          )
        }

        StatusPill(
          text = record.status,
          type = when (record.status.lowercase()) {
            "good", "optimal" -> StatusType.GOOD
            "needs attention" -> StatusType.WARNING
            else -> StatusType.MODERATE
          }
        )
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = SoilBorderLight
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Nitrogen
        Column {
          Text(
            text = "N (Nitrogen)",
            style = MaterialTheme.typography.labelSmall,
            color = SoilTextMuted,
            fontSize = 10.sp
          )
          Text(
            text = "${record.nitrogenMgKg.toInt()}",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = SoilNitrogenBar
          )
        }

        // Phosphorus
        Column {
          Text(
            text = "P (Phosphate)",
            style = MaterialTheme.typography.labelSmall,
            color = SoilTextMuted,
            fontSize = 10.sp
          )
          Text(
            text = "${record.phosphorusMgKg.toInt()}",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = SoilPhosphorusBar
          )
        }

        // Potassium
        Column {
          Text(
            text = "K (Potassium)",
            style = MaterialTheme.typography.labelSmall,
            color = SoilTextMuted,
            fontSize = 10.sp
          )
          Text(
            text = "${record.potassiumMgKg.toInt()}",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = SoilPotassiumBar
          )
        }

        // Score
        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "Health",
            style = MaterialTheme.typography.labelSmall,
            color = SoilTextMuted,
            fontSize = 10.sp
          )
          Text(
            text = "${String.format("%.1f", record.healthScore)} / 10",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = SoilForestGreen
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Tap to view test details",
          style = MaterialTheme.typography.labelSmall,
          color = SoilEmerald,
          fontSize = 11.sp
        )
        Icon(
          imageVector = Icons.Default.ChevronRight,
          contentDescription = null,
          tint = SoilEmerald,
          modifier = Modifier.size(16.dp)
        )
      }
    }
  }
}

@Composable
fun SoilTestDetailDialog(
  record: SoilTestRecord,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = SoilCardBg),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("soil_detail_modal")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .heightIn(max = 680.dp)
          .verticalScroll(rememberScrollState())
          .padding(20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Test Details",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            color = SoilForestGreen
          )
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = SoilGreenLight)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text("Location", style = MaterialTheme.typography.labelSmall, color = SoilTextMuted)
              Text(record.fieldName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.End) {
              Text("Date & Time", style = MaterialTheme.typography.labelSmall, color = SoilTextMuted)
              Text("${record.date}, ${record.time}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Visual NPK Balance Chart for this test
        NpkSoilBalanceChart(
          record = record,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        MetadataRow(label = "Nitrogen (N)", value = "${record.nitrogenMgKg.toInt()} mg/kg (${record.nitrogenStatus})", isHighlight = true)
        MetadataRow(label = "Phosphorus (P)", value = "${record.phosphorusMgKg.toInt()} mg/kg (${record.phosphorusStatus})", isHighlight = true)
        MetadataRow(label = "Potassium (K⁺)", value = "${record.potassiumMgKg.toInt()} mg/kg (${record.potassiumStatus})", isHighlight = true)
        MetadataRow(label = "Soil Health Score", value = "${record.healthScore} / 10")
        MetadataRow(label = "Sensor Reading Status", value = record.readingStatus)
        MetadataRow(label = "Calibration Status", value = record.calibrationStatus)
        MetadataRow(label = "Soil pH (Estimated)", value = "${record.phLevel}")
        MetadataRow(label = "Moisture Level", value = "${record.moisturePercent}%")

        Spacer(modifier = Modifier.height(12.dp))

        Text("Notes & Observations", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = SoilForestGreen)
        Spacer(modifier = Modifier.height(4.dp))
        Text(record.notes, style = MaterialTheme.typography.bodySmall, color = SoilTextDark, lineHeight = 18.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = SoilForestGreen),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Done")
        }
      }
    }
  }
}
