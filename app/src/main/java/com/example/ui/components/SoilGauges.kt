package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SemicircularHealthGaugeCard(
  score: Double = 8.2,
  maxScore: Double = 10.0,
  status: String = "GOOD",
  lastTestDate: String = "02 September 2026",
  fieldName: String = "Demo Field",
  modifier: Modifier = Modifier
) {
  val animatedProgress by animateFloatAsState(
    targetValue = (score / maxScore).toFloat().coerceIn(0f, 1f),
    animationSpec = tween(durationMillis = 1000),
    label = "health_gauge"
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("soil_health_score_card"),
    shape = RoundedCornerShape(32.dp),
    colors = CardDefaults.cardColors(containerColor = SoilCardBg),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorderLight)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(32.dp))
    ) {
      // Decorative corner circle from High Density HTML: absolute -top-10 -right-10 w-32 h-32 bg-[#E8F5E9] rounded-full opacity-50
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .offset(x = 24.dp, y = (-24).dp)
          .size(110.dp)
          .clip(CircleShape)
          .background(SoilGreenLight.copy(alpha = 0.6f))
      )

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "SOIL HEALTH SCORE",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = SoilTextMuted,
          letterSpacing = 1.5.sp,
          fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Semicircular Canvas Gauge
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
          contentAlignment = Alignment.Center
        ) {
          Canvas(
            modifier = Modifier
              .size(200.dp, 110.dp)
              .testTag("semicircular_gauge_canvas")
          ) {
            val strokeWidth = 14.dp.toPx()
            val diameter = size.width - strokeWidth
            val arcSize = Size(diameter, diameter)
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

            // Background Track Arc (180 degrees)
            drawArc(
              color = SoilBorder,
              startAngle = 180f,
              sweepAngle = 180f,
              useCenter = false,
              topLeft = topLeft,
              size = arcSize,
              style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Active Arc
            val sweep = 180f * animatedProgress
            if (sweep > 0f) {
              drawArc(
                color = SoilEmerald,
                startAngle = 180f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
              )
            }
          }

          // Center Score Text
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 28.dp)
          ) {
            Row(
              verticalAlignment = Alignment.Bottom
            ) {
              Text(
                text = String.format("%.1f", score),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = SoilForestGreen,
                fontSize = 38.sp
              )
              Text(
                text = " / 10",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SoilTextMuted,
                modifier = Modifier.padding(bottom = 6.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Status pill
        Surface(
          color = SoilEmerald,
          shape = CircleShape,
          shadowElevation = 3.dp
        ) {
          Text(
            text = status.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 11.sp,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        HorizontalDivider(
          color = SoilBorderLight
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Test metadata row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Last Tested",
              style = MaterialTheme.typography.labelSmall,
              color = SoilTextMuted,
              fontSize = 10.sp
            )
            Text(
              text = lastTestDate,
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.SemiBold,
              color = SoilTextDark
            )
          }
          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "Field Location",
              style = MaterialTheme.typography.labelSmall,
              color = SoilTextMuted,
              fontSize = 10.sp
            )
            Text(
              text = fieldName,
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.SemiBold,
              color = SoilEmerald
            )
          }
        }
      }
    }
  }
}
