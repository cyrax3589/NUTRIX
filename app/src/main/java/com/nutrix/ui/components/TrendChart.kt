package com.nutrix.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nutrix.model.SoilTestRecord
import com.nutrix.ui.theme.*

@Composable
fun SoilNutrientTrendChart(
  history: List<SoilTestRecord>,
  modifier: Modifier = Modifier
) {
  var selectedNutrient by remember { mutableStateOf("NPK") } // "NPK", "N", "P", "K"
  val sortedList = remember(history) { history.take(6).reversed() }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("soil_trend_chart_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = SoilCardBg),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorderLight)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "NPK TREND OVER TIME",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = SoilTextMuted,
            letterSpacing = 1.sp,
            fontSize = 10.sp
          )
          Text(
            text = when (selectedNutrient) {
              "NPK" -> "Macro Balance (% of Target)"
              "N" -> "Nitrogen (NO₃⁻) • mg/kg"
              "P" -> "Phosphorus (P₂O₅) • mg/kg"
              else -> "Potassium (K⁺) • mg/kg"
            },
            style = MaterialTheme.typography.bodySmall,
            color = SoilForestGreen,
            fontWeight = FontWeight.SemiBold
          )
        }

        // Toggle Switch for NPK / N / P / K
        Surface(
          color = SoilGreenLight,
          shape = CircleShape,
          border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorderLight)
        ) {
          Row(modifier = Modifier.padding(2.dp)) {
            NutrientTabButton(
              text = "NPK",
              isSelected = selectedNutrient == "NPK",
              accentColor = SoilForestGreen,
              onClick = { selectedNutrient = "NPK" }
            )
            NutrientTabButton(
              text = "N",
              isSelected = selectedNutrient == "N",
              accentColor = SoilNitrogenBar,
              onClick = { selectedNutrient = "N" }
            )
            NutrientTabButton(
              text = "P",
              isSelected = selectedNutrient == "P",
              accentColor = SoilPhosphorusBar,
              onClick = { selectedNutrient = "P" }
            )
            NutrientTabButton(
              text = "K⁺",
              isSelected = selectedNutrient == "K",
              accentColor = SoilPotassiumBar,
              onClick = { selectedNutrient = "K" }
            )
          }
        }
      }

      if (selectedNutrient == "NPK") {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          TrendLegendItem(color = SoilNitrogenBar, label = "Nitrogen (N)")
          TrendLegendItem(color = SoilPhosphorusBar, label = "Phosphorus (P)")
          TrendLegendItem(color = SoilPotassiumBar, label = "Potassium (K)")
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Canvas Trend Line & Area Chart
      val chartColor = when (selectedNutrient) {
        "N" -> SoilNitrogenBar
        "P" -> SoilPhosphorusBar
        "K" -> SoilPotassiumBar
        else -> SoilForestGreen
      }

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(140.dp)
      ) {
        Canvas(
          modifier = Modifier
            .fillMaxSize()
            .testTag("trend_canvas")
        ) {
          if (sortedList.size < 2) return@Canvas

          val width = size.width
          val height = size.height - 20.dp.toPx()
          val stepX = width / (sortedList.size - 1)

          // Background Grid Lines
          val gridLineCount = 3
          for (i in 0..gridLineCount) {
            val y = height * (i.toFloat() / gridLineCount)
            drawLine(
              color = Color(0xFFEFF2EB),
              start = Offset(0f, y),
              end = Offset(width, y),
              strokeWidth = 1.dp.toPx()
            )
          }

          if (selectedNutrient == "NPK") {
            // Draw N, P, K normalized (% of optimal target: N:230, P:22.5, K:65)
            val seriesList = listOf(
              Triple(sortedList.map { (it.nitrogenMgKg / 230.0).toFloat() }, SoilNitrogenBar, "N"),
              Triple(sortedList.map { (it.phosphorusMgKg / 22.5).toFloat() }, SoilPhosphorusBar, "P"),
              Triple(sortedList.map { (it.potassiumMgKg / 65.0).toFloat() }, SoilPotassiumBar, "K")
            )

            // Optimal 100% baseline line
            val optY = height - (0.65f * height)
            drawLine(
              color = SoilEmerald.copy(alpha = 0.4f),
              start = Offset(0f, optY),
              end = Offset(width, optY),
              strokeWidth = 1.5.dp.toPx()
            )

            seriesList.forEach { (seriesValues, color, _) ->
              val points = seriesValues.mapIndexed { index, v ->
                val normalized = ((v - 0.4f) / (1.5f - 0.4f)).coerceIn(0.08f, 0.95f)
                val x = index * stepX
                val y = height - (normalized * height)
                Offset(x, y)
              }

              val strokePath = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                  lineTo(points[i].x, points[i].y)
                }
              }

              drawPath(
                path = strokePath,
                color = color,
                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
              )

              points.forEach { p ->
                drawCircle(color = Color.White, radius = 4.dp.toPx(), center = p)
                drawCircle(color = color, radius = 2.5.dp.toPx(), center = p)
              }
            }
          } else {
            val (values, minVal, maxVal) = when (selectedNutrient) {
              "N" -> Triple(sortedList.map { it.nitrogenMgKg.toFloat() }, 100f, 320f)
              "P" -> Triple(sortedList.map { it.phosphorusMgKg.toFloat() }, 8f, 40f)
              else -> Triple(sortedList.map { it.potassiumMgKg.toFloat() }, 30f, 90f)
            }

            val points = values.mapIndexed { index, v ->
              val normalized = ((v - minVal) / (maxVal - minVal)).coerceIn(0.1f, 0.95f)
              val x = index * stepX
              val y = height - (normalized * height)
              Offset(x, y)
            }

            // Draw Fill Area
            val fillPath = Path().apply {
              moveTo(points.first().x, height)
              points.forEach { lineTo(it.x, it.y) }
              lineTo(points.last().x, height)
              close()
            }

            drawPath(
              path = fillPath,
              brush = Brush.verticalGradient(
                colors = listOf(chartColor.copy(alpha = 0.25f), chartColor.copy(alpha = 0.02f)),
                startY = 0f,
                endY = height
              )
            )

            // Draw Smooth Line
            val strokePath = Path().apply {
              moveTo(points.first().x, points.first().y)
              for (i in 1 until points.size) {
                lineTo(points[i].x, points[i].y)
              }
            }

            drawPath(
              path = strokePath,
              color = chartColor,
              style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )

            // Draw Points and Data Circles
            points.forEachIndexed { _, p ->
              drawCircle(color = Color.White, radius = 5.dp.toPx(), center = p)
              drawCircle(color = chartColor, radius = 3.dp.toPx(), center = p)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Dates labels below chart
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        sortedList.forEach { record ->
          Text(
            text = record.date.substringBefore(" "),
            style = MaterialTheme.typography.labelSmall,
            color = SoilTextMuted,
            fontSize = 10.sp
          )
        }
      }
    }
  }
}

@Composable
fun TrendLegendItem(
  color: Color,
  label: String
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Box(
      modifier = Modifier
        .size(8.dp)
        .clip(CircleShape)
        .background(color)
    )
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = SoilTextDark,
      fontSize = 10.sp
    )
  }
}

@Composable
fun NutrientTabButton(
  text: String,
  isSelected: Boolean,
  accentColor: Color,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .background(if (isSelected) accentColor else Color.Transparent)
      .clickable(onClick = onClick)
      .padding(horizontal = 12.dp, vertical = 6.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelMedium,
      fontWeight = FontWeight.Bold,
      color = if (isSelected) Color.White else SoilTextDark
    )
  }
}
