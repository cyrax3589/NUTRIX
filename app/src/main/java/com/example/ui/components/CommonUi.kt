package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DemoModeBadge(modifier: Modifier = Modifier) {
  Surface(
    modifier = modifier.testTag("demo_mode_badge"),
    color = SoilGreenLight,
    shape = CircleShape
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(8.dp)
          .clip(CircleShape)
          .background(SoilGreenAccent)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = "DEMO MODE",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Black,
        color = SoilForestGreen,
        fontSize = 10.sp,
        letterSpacing = 0.5.sp
      )
    }
  }
}

@Composable
fun FieldWeatherCard(
  location: String = "Bengaluru, KA",
  temperature: String = "28°C",
  weatherCondition: String = "Sunny",
  humidity: String = "65% Humidity",
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 4.dp)
      .testTag("field_weather_card"),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(vertical = 2.dp)
    ) {
      Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription = "Location",
        tint = SoilTextMuted,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = location,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        color = SoilTextDark
      )
    }

    Surface(
      color = Color.White.copy(alpha = 0.95f),
      shape = RoundedCornerShape(8.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorderLight),
      shadowElevation = 1.dp
    ) {
      Text(
        text = "$temperature • $weatherCondition",
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Medium,
        color = SoilTextDark,
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
      )
    }
  }
}

@Composable
fun StatusPill(
  text: String,
  type: StatusType = StatusType.GOOD,
  modifier: Modifier = Modifier
) {
  val (bgColor, textColor) = when (type) {
    StatusType.GOOD, StatusType.OPTIMAL -> SoilEmerald to Color.White
    StatusType.MODERATE -> SoilAmber to Color.White
    StatusType.WARNING, StatusType.NEEDS_ATTENTION -> SoilRed to Color.White
  }

  Surface(
    modifier = modifier,
    color = bgColor,
    shape = CircleShape,
    shadowElevation = 2.dp
  ) {
    Text(
      text = text.uppercase(),
      style = MaterialTheme.typography.labelSmall,
      fontWeight = FontWeight.Black,
      color = textColor,
      fontSize = 10.sp,
      letterSpacing = 0.5.sp,
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
    )
  }
}

enum class StatusType {
  GOOD, OPTIMAL, MODERATE, WARNING, NEEDS_ATTENTION
}

@Composable
fun PrototypeDisclaimerBanner(modifier: Modifier = Modifier) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag("prototype_disclaimer"),
    color = SoilAmberLight.copy(alpha = 0.7f),
    shape = RoundedCornerShape(12.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, SoilAmber.copy(alpha = 0.4f))
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      verticalAlignment = Alignment.Top
    ) {
      Icon(
        imageVector = Icons.Outlined.Info,
        contentDescription = "Disclaimer",
        tint = SoilOrange,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(10.dp))
      Text(
        text = "Field sensor readings & AI recommendations are indicative prototypes for demonstration. Verify with local agricultural extension services.",
        style = MaterialTheme.typography.bodySmall,
        color = SoilDarkBrown,
        lineHeight = 16.sp
      )
    }
  }
}
