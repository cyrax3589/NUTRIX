package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Agriculture
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.WaterDrop
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
import com.example.model.CropRecommendation
import com.example.ui.components.DemoModeBadge
import com.example.ui.components.NutrixLogo
import com.example.ui.components.PrototypeDisclaimerBanner
import com.example.ui.theme.*

@Composable
fun RecommendationsScreen(
  modifier: Modifier = Modifier
) {
  val latestTest by SampleData.currentLatestTest.collectAsState()
  val profile by SampleData.farmerProfile.collectAsState()
  var selectedCropForModal by remember { mutableStateOf<CropRecommendation?>(null) }

  Surface(
    modifier = modifier
      .fillMaxSize()
      .testTag("ai_recommendations_screen"),
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
                text = "AI Soil Tips",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = SoilEmerald,
                letterSpacing = (-0.5).sp
              )
              Text(
                text = "CROP SUITABILITY & FERTILIZER",
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

        // Context Banner: "Based on your latest soil test"
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = SoilGreenLight),
          border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorderLight)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = null,
                tint = SoilForestGreen,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Based on test from ${latestTest.date}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = SoilForestGreen
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              ContextChipItem(label = "Selected Crop", value = profile.currentCrop)
              ContextChipItem(label = "Soil Type", value = profile.soilType)
              ContextChipItem(label = "Location", value = latestTest.fieldName)
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fertilizer Guidance Card
        FertilizerGuidanceCard()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "CROP SUITABILITY RANKINGS",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = SoilTextMuted,
          letterSpacing = 1.sp,
          fontSize = 10.sp
        )

        Spacer(modifier = Modifier.height(10.dp))
      }

      // Crop Suggestion Cards
      items(SampleData.cropRecommendations, key = { it.id }) { crop ->
        CropSuggestionCard(
          crop = crop,
          onViewDetails = { selectedCropForModal = crop }
        )
        Spacer(modifier = Modifier.height(10.dp))
      }

      item {
        Spacer(modifier = Modifier.height(10.dp))
        PrototypeDisclaimerBanner()
      }
    }
  }

  selectedCropForModal?.let { crop ->
    CropDetailDialog(
      crop = crop,
      onDismiss = { selectedCropForModal = null }
    )
  }
}

@Composable
fun ContextChipItem(label: String, value: String) {
  Column {
    Text(text = label, style = MaterialTheme.typography.labelSmall, color = SoilTextMuted, fontSize = 10.sp)
    Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = SoilTextDark)
  }
}

@Composable
fun FertilizerGuidanceCard(modifier: Modifier = Modifier) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("fertilizer_guidance_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = SoilCardBg),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorderLight)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(SoilGreenLight),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Outlined.Eco,
            contentDescription = null,
            tint = SoilEmerald,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "Fertilizer Guidance",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = SoilForestGreen
          )
          Text(
            text = "Tailored to K⁺ (62 mg/kg) & NO₃⁻ (0.32 mM)",
            style = MaterialTheme.typography.bodySmall,
            color = SoilTextMuted
          )
        }
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 12.dp),
        color = SoilBorderLight
      )

      BulletGuidanceItem("• Potassium level is within the configured target range for vegetative growth.")
      BulletGuidanceItem("• Nitrate is adequate for the selected crop; avoid heavy nitrogen top-dressing.")
      BulletGuidanceItem("• Consider balanced organic fertilizer application based on crop requirements.")

      Spacer(modifier = Modifier.height(10.dp))

      Surface(
        color = SoilGreenLight,
        shape = RoundedCornerShape(10.dp)
      ) {
        Text(
          text = "Indicative advice only. Dosages should be calibrated with regional farm advisory centers.",
          style = MaterialTheme.typography.labelSmall,
          color = SoilForestGreen,
          fontSize = 11.sp,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
      }
    }
  }
}

@Composable
fun BulletGuidanceItem(text: String) {
  Text(
    text = text,
    style = MaterialTheme.typography.bodySmall,
    color = SoilTextDark,
    lineHeight = 18.sp,
    modifier = Modifier.padding(vertical = 2.dp)
  )
}

@Composable
fun CropSuggestionCard(
  crop: CropRecommendation,
  onViewDetails: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("crop_card_${crop.name.lowercase().replace(" ", "_")}"),
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
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(SoilSurfaceCream)
              .border(2.dp, SoilGreenLight, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Grass,
              contentDescription = crop.name,
              tint = SoilEmerald,
              modifier = Modifier.size(22.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = crop.name,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = SoilTextDark
            )
            Text(
              text = crop.regionalName,
              style = MaterialTheme.typography.bodySmall,
              color = SoilTextMuted,
              fontSize = 11.sp
            )
          }
        }

        // Suitability Badge
        Surface(
          color = SoilGreenLight,
          shape = CircleShape
        ) {
          Text(
            text = "${crop.suitabilityPercent}% MATCH",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Black,
            color = SoilForestGreen,
            fontSize = 10.sp,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = crop.guidanceNote,
        style = MaterialTheme.typography.bodySmall,
        color = SoilTextDark,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Outlined.WaterDrop,
            contentDescription = null,
            tint = SoilNitrateColor,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = crop.waterRequirement.substringBefore(" ("),
            style = MaterialTheme.typography.labelSmall,
            color = SoilTextMuted,
            fontSize = 11.sp
          )
        }

        TextButton(
          onClick = onViewDetails,
          modifier = Modifier.testTag("view_details_${crop.name.lowercase().replace(" ", "_")}")
        ) {
          Text(
            text = "Details",
            fontWeight = FontWeight.Bold,
            color = SoilEmerald,
            fontSize = 12.sp
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
}

@Composable
fun CropDetailDialog(
  crop: CropRecommendation,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = SoilCardBg),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("crop_detail_modal")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = crop.name,
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Black,
              color = SoilForestGreen
            )
            Text(
              text = "${crop.suitabilityPercent}% Suitability Score",
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.Bold,
              color = SoilEmerald
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        MetadataRow(label = "Regional Name", value = crop.regionalName)
        MetadataRow(label = "Recommended Soil", value = crop.soilType)
        MetadataRow(label = "Potassium Need", value = crop.potassiumNeed)
        MetadataRow(label = "Nitrate Need", value = crop.nitrogenNeed)
        MetadataRow(label = "Expected Yield", value = crop.yieldExpectation)
        MetadataRow(label = "Crop Duration", value = crop.growthDuration)
        MetadataRow(label = "Water Strategy", value = crop.waterRequirement)

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Cultivation Advice",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = SoilForestGreen
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = crop.guidanceNote,
          style = MaterialTheme.typography.bodySmall,
          color = SoilTextDark,
          lineHeight = 18.sp
        )

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
