package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun NutrientStatusCard(
  ionName: String,
  chemicalFormula: String,
  value: String,
  unit: String,
  status: String,
  levelFraction: Float, // 0.0 to 1.0 (e.g. 0.75 for Good)
  accentColor: Color = SoilEmerald,
  lightColor: Color = SoilGreenLight,
  targetRangeText: String = "Target: 50-80 mg/kg",
  modifier: Modifier = Modifier
) {
  val animatedLevel by animateFloatAsState(
    targetValue = levelFraction.coerceIn(0f, 1f),
    animationSpec = tween(800),
    label = "nutrient_level"
  )

  Card(
    modifier = modifier
      .testTag("nutrient_card_${chemicalFormula.lowercase().replace("⁺", "").replace("⁻", "")}"),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = SoilCardBg),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorderLight)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Top icon and ion title
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(lightColor),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = chemicalFormula,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Black,
            color = accentColor,
            fontSize = 12.sp
          )
        }
        Text(
          text = ionName.uppercase(),
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = SoilTextMuted,
          fontSize = 10.sp,
          letterSpacing = 0.5.sp
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Big Value & Unit
      Row(
        verticalAlignment = Alignment.Bottom
      ) {
        Text(
          text = value,
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          color = SoilTextDark,
          fontSize = 24.sp
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = unit,
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Normal,
          color = SoilTextMuted,
          fontSize = 10.sp,
          modifier = Modifier.padding(bottom = 3.dp)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Thin progress bar
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(4.dp)
          .clip(CircleShape)
          .background(SoilBorder)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth(animatedLevel)
            .fillMaxHeight()
            .clip(CircleShape)
            .background(accentColor)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Status text
      Text(
        text = if (chemicalFormula.contains("K")) "HEALTHY RANGE" else "OPTIMAL LEVEL",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = accentColor,
        fontSize = 10.sp,
        letterSpacing = 0.5.sp
      )
    }
  }
}

@Composable
fun NutrientRangeBar(
  levelFraction: Float,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier.fillMaxWidth()) {
    // Progress Bar Track
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
        .clip(RoundedCornerShape(4.dp))
        .background(Color(0xFFE8ECE5))
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth(levelFraction)
          .fillMaxHeight()
          .clip(RoundedCornerShape(4.dp))
          .background(accentColor)
      )
    }

    Spacer(modifier = Modifier.height(4.dp))

    // Labels under bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(text = "Low", style = MaterialTheme.typography.labelSmall, color = SoilTextMuted, fontSize = 9.sp)
      Text(text = "Mod", style = MaterialTheme.typography.labelSmall, color = SoilTextMuted, fontSize = 9.sp)
      Text(text = "Good", style = MaterialTheme.typography.labelSmall, color = SoilTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
      Text(text = "High", style = MaterialTheme.typography.labelSmall, color = SoilTextMuted, fontSize = 9.sp)
    }
  }
}
