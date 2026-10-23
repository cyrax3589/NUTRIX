package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SoilTestRecord
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

enum class NpkChartMode {
  BAR_COMPARE,
  EQUILIBRIUM_RADAR
}

enum class SelectedNutrient {
  NITROGEN,
  PHOSPHORUS,
  POTASSIUM
}

data class NpkNutrientData(
  val symbol: String,
  val name: String,
  val chemicalIon: String,
  val value: Double,
  val unit: String,
  val targetMin: Double,
  val targetMax: Double,
  val targetOptimal: Double,
  val status: String,
  val maxScale: Double,
  val barColor: Color,
  val lightColor: Color,
  val agronomicRole: String,
  val recommendation: String
)

@Composable
fun NpkSoilBalanceChart(
  record: SoilTestRecord,
  modifier: Modifier = Modifier,
  initialMode: NpkChartMode = NpkChartMode.BAR_COMPARE
) {
  var chartMode by remember { mutableStateOf(initialMode) }
  var selectedNutrient by remember { mutableStateOf(SelectedNutrient.NITROGEN) }

  val nitrogenData = remember(record) {
    val status = record.nitrogenStatus
    NpkNutrientData(
      symbol = "N",
      name = "Nitrogen",
      chemicalIon = "NO₃⁻ / NH₄⁺",
      value = record.nitrogenMgKg,
      unit = "mg/kg",
      targetMin = 180.0,
      targetMax = 280.0,
      targetOptimal = 230.0,
      status = status,
      maxScale = 350.0,
      barColor = SoilNitrogenBar,
      lightColor = SoilNitrogenLight,
      agronomicRole = "Powers vigorous vegetative foliage, stem growth, and green chlorophyll synthesis.",
      recommendation = when (status) {
        "Low" -> "Nitrogen level is sub-optimal. Apply balanced compost or split urea top-dressing during tillering."
        "High" -> "High nitrogen reserve. Avoid excessive nitrogenous fertilizer to minimize stem lodging and pest risk."
        else -> "Optimal vegetative nitrogen balance. Maintain standard irrigation and organic matter."
      }
    )
  }

  val phosphorusData = remember(record) {
    val status = record.phosphorusStatus
    NpkNutrientData(
      symbol = "P",
      name = "Phosphorus",
      chemicalIon = "Available P₂O₅",
      value = record.phosphorusMgKg,
      unit = "mg/kg",
      targetMin = 15.0,
      targetMax = 30.0,
      targetOptimal = 22.5,
      status = status,
      maxScale = 45.0,
      barColor = SoilPhosphorusBar,
      lightColor = SoilPhosphorusLight,
      agronomicRole = "Stimulates deep fibrous root development, seed germination, and ATP energy transfer.",
      recommendation = when (status) {
        "Low" -> "Phosphorus is depleted. Apply single superphosphate (SSP) or rock phosphate near root zones."
        "High" -> "High phosphorus reserve. Ensure good soil aeration; reduce phosphate-heavy inputs."
        else -> "Healthy phosphorus reserve. Excellent for seedling vigor and robust root establishment."
      }
    )
  }

  val potassiumData = remember(record) {
    val status = record.potassiumStatus
    NpkNutrientData(
      symbol = "K",
      name = "Potassium",
      chemicalIon = "Potash (K⁺)",
      value = record.potassiumMgKg,
      unit = "mg/kg",
      targetMin = 50.0,
      targetMax = 80.0,
      targetOptimal = 65.0,
      status = status,
      maxScale = 100.0,
      barColor = SoilPotassiumBar,
      lightColor = SoilPotassiumLight,
      agronomicRole = "Regulates stomata, improves drought tolerance, disease resistance, and grain weight.",
      recommendation = when (status) {
        "Low" -> "Potassium level is deficient. Apply Muriate of Potash (MOP) or wood ash to fortify culm strength."
        "High" -> "Abundant potassium. Facilitates carbohydrate transport and superior grain plumpness."
        else -> "Optimal potassium balance. Promotes disease resilience and efficient water utilization."
      }
    )
  }

  val activeNutrientData = when (selectedNutrient) {
    SelectedNutrient.NITROGEN -> nitrogenData
    SelectedNutrient.PHOSPHORUS -> phosphorusData
    SelectedNutrient.POTASSIUM -> potassiumData
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("npk_chart_component"),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = SoilCardBg),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorder)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      // Top Header: Title, Field and View Mode Selector
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.BarChart,
              contentDescription = null,
              tint = SoilForestGreen,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "NPK NUTRIENT BALANCE",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = SoilForestGreen,
              letterSpacing = 1.sp,
              fontSize = 11.sp
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Nitrogen • Phosphorus • Potassium",
            style = MaterialTheme.typography.bodySmall,
            color = SoilTextMuted,
            fontSize = 12.sp
          )
        }

        // Mode switch pills (Bar vs Radar)
        Surface(
          color = SoilGreenLight,
          shape = CircleShape,
          border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorderLight)
        ) {
          Row(
            modifier = Modifier.padding(2.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            ChartModeToggleButton(
              icon = Icons.Default.BarChart,
              label = "Bars",
              isSelected = chartMode == NpkChartMode.BAR_COMPARE,
              onClick = { chartMode = NpkChartMode.BAR_COMPARE }
            )
            ChartModeToggleButton(
              icon = Icons.Default.Balance,
              label = "Radar",
              isSelected = chartMode == NpkChartMode.EQUILIBRIUM_RADAR,
              onClick = { chartMode = NpkChartMode.EQUILIBRIUM_RADAR }
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Animated Chart Canvas Area
      AnimatedContent(
        targetState = chartMode,
        transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(200)) },
        label = "chart_mode_transition"
      ) { mode ->
        when (mode) {
          NpkChartMode.BAR_COMPARE -> {
            Column(
              modifier = Modifier.fillMaxWidth(),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              NpkBarComparisonRow(
                data = nitrogenData,
                isSelected = selectedNutrient == SelectedNutrient.NITROGEN,
                onClick = { selectedNutrient = SelectedNutrient.NITROGEN }
              )
              NpkBarComparisonRow(
                data = phosphorusData,
                isSelected = selectedNutrient == SelectedNutrient.PHOSPHORUS,
                onClick = { selectedNutrient = SelectedNutrient.PHOSPHORUS }
              )
              NpkBarComparisonRow(
                data = potassiumData,
                isSelected = selectedNutrient == SelectedNutrient.POTASSIUM,
                onClick = { selectedNutrient = SelectedNutrient.POTASSIUM }
              )
            }
          }
          NpkChartMode.EQUILIBRIUM_RADAR -> {
            NpkRadarEquilibriumCanvas(
              nitrogen = nitrogenData,
              phosphorus = phosphorusData,
              potassium = potassiumData,
              selectedNutrient = selectedNutrient,
              onSelectNutrient = { selectedNutrient = it }
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Nutrient Quick Selector Chips (N, P, K)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        NutrientSelectorChip(
          symbol = "N",
          label = "Nitrogen",
          valueText = "${record.nitrogenMgKg.toInt()} mg/kg",
          status = record.nitrogenStatus,
          accentColor = SoilNitrogenBar,
          isSelected = selectedNutrient == SelectedNutrient.NITROGEN,
          onClick = { selectedNutrient = SelectedNutrient.NITROGEN },
          modifier = Modifier.weight(1f)
        )
        NutrientSelectorChip(
          symbol = "P",
          label = "Phosphorus",
          valueText = "${record.phosphorusMgKg.toInt()} mg/kg",
          status = record.phosphorusStatus,
          accentColor = SoilPhosphorusBar,
          isSelected = selectedNutrient == SelectedNutrient.PHOSPHORUS,
          onClick = { selectedNutrient = SelectedNutrient.PHOSPHORUS },
          modifier = Modifier.weight(1f)
        )
        NutrientSelectorChip(
          symbol = "K",
          label = "Potassium",
          valueText = "${record.potassiumMgKg.toInt()} mg/kg",
          status = record.potassiumStatus,
          accentColor = SoilPotassiumBar,
          isSelected = selectedNutrient == SelectedNutrient.POTASSIUM,
          onClick = { selectedNutrient = SelectedNutrient.POTASSIUM },
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Selected Nutrient Detailed Telemetry Box
      NutrientDetailCard(nutrient = activeNutrientData)

      Spacer(modifier = Modifier.height(12.dp))

      // Soil N:P:K Ratio Banner
      Surface(
        color = SoilGreenSurface,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorderLight)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = null,
              tint = SoilEmerald,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
              Text(
                text = "SOIL N:P:K RATIO",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = SoilTextMuted,
                fontSize = 10.sp
              )
              Text(
                text = record.npkRatioText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Black,
                color = SoilForestGreen
              )
            }
          }

          Surface(
            color = Color.White,
            shape = CircleShape,
            border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorderLight)
          ) {
            Text(
              text = if (record.status == "Good" || record.status == "Optimal") "Well Balanced" else "Adjustment Recommended",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = if (record.status == "Good" || record.status == "Optimal") SoilEmerald else SoilOrange,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
              fontSize = 10.sp
            )
          }
        }
      }
    }
  }
}

@Composable
fun NpkBarComparisonRow(
  data: NpkNutrientData,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val animatedFraction by animateFloatAsState(
    targetValue = (data.value / data.maxScale).toFloat().coerceIn(0.05f, 1f),
    animationSpec = tween(700),
    label = "bar_progress_${data.symbol}"
  )

  val minFraction = (data.targetMin / data.maxScale).toFloat()
  val maxFraction = (data.targetMax / data.maxScale).toFloat()

  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(if (isSelected) data.lightColor.copy(alpha = 0.4f) else Color.Transparent)
      .clickable(onClick = onClick)
      .padding(horizontal = 8.dp, vertical = 6.dp)
  ) {
    // Header row: Symbol, Name, Measured Value, Status
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(data.barColor),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = data.symbol,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Black,
            color = Color.White,
            fontSize = 12.sp
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = data.name,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold,
          color = SoilTextDark
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "(${data.chemicalIon})",
          style = MaterialTheme.typography.labelSmall,
          color = SoilTextMuted,
          fontSize = 10.sp
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "${data.value.toInt()} ${data.unit}",
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Black,
          color = data.barColor
        )
        Spacer(modifier = Modifier.width(6.dp))
        StatusTag(status = data.status)
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    // Canvas Bar with Target Range Bracket
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(20.dp)
    ) {
      Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val cornerRadius = CornerRadius(h / 2f, h / 2f)

        // 1. Background full track
        drawRoundRect(
          color = Color(0xFFF1F3EE),
          topLeft = Offset(0f, 0f),
          size = Size(w, h),
          cornerRadius = cornerRadius
        )

        // 2. Shaded Optimal Target Zone bracket
        val targetLeft = w * minFraction
        val targetWidth = w * (maxFraction - minFraction)
        drawRect(
          color = SoilEmerald.copy(alpha = 0.12f),
          topLeft = Offset(targetLeft, 0f),
          size = Size(targetWidth, h)
        )
        // Target boundary vertical lines
        drawLine(
          color = SoilEmerald.copy(alpha = 0.5f),
          start = Offset(targetLeft, 0f),
          end = Offset(targetLeft, h),
          strokeWidth = 1.5.dp.toPx()
        )
        drawLine(
          color = SoilEmerald.copy(alpha = 0.5f),
          end = Offset(targetLeft + targetWidth, h),
          start = Offset(targetLeft + targetWidth, 0f),
          strokeWidth = 1.5.dp.toPx()
        )

        // 3. Current Level Progress Bar
        val barWidth = (w * animatedFraction).coerceAtMost(w)
        drawRoundRect(
          brush = Brush.horizontalGradient(
            colors = listOf(data.barColor.copy(alpha = 0.7f), data.barColor)
          ),
          topLeft = Offset(0f, 0f),
          size = Size(barWidth, h),
          cornerRadius = cornerRadius
        )

        // 4. Data pin indicator on the bar edge
        drawCircle(
          color = Color.White,
          radius = 5.dp.toPx(),
          center = Offset(barWidth, h / 2f)
        )
        drawCircle(
          color = data.barColor,
          radius = 3.dp.toPx(),
          center = Offset(barWidth, h / 2f)
        )
      }
    }

    // Target range label below bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 2.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = "0",
        style = MaterialTheme.typography.labelSmall,
        color = SoilTextMuted,
        fontSize = 9.sp
      )
      Text(
        text = "Target Window: ${data.targetMin.toInt()} – ${data.targetMax.toInt()} ${data.unit}",
        style = MaterialTheme.typography.labelSmall,
        color = SoilTextMuted,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium
      )
      Text(
        text = "${data.maxScale.toInt()}",
        style = MaterialTheme.typography.labelSmall,
        color = SoilTextMuted,
        fontSize = 9.sp
      )
    }
  }
}

@Composable
fun NpkRadarEquilibriumCanvas(
  nitrogen: NpkNutrientData,
  phosphorus: NpkNutrientData,
  potassium: NpkNutrientData,
  selectedNutrient: SelectedNutrient,
  onSelectNutrient: (SelectedNutrient) -> Unit,
  modifier: Modifier = Modifier
) {
  // Normalize each nutrient against its target optimal (1.0 = optimal)
  val nRatio = (nitrogen.value / nitrogen.targetOptimal).toFloat().coerceIn(0.2f, 1.4f)
  val pRatio = (phosphorus.value / phosphorus.targetOptimal).toFloat().coerceIn(0.2f, 1.4f)
  val kRatio = (potassium.value / potassium.targetOptimal).toFloat().coerceIn(0.2f, 1.4f)

  val animN by animateFloatAsState(targetValue = nRatio, animationSpec = tween(800), label = "radar_n")
  val animP by animateFloatAsState(targetValue = pRatio, animationSpec = tween(800), label = "radar_p")
  val animK by animateFloatAsState(targetValue = kRatio, animationSpec = tween(800), label = "radar_k")

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(210.dp)
      .testTag("npk_radar_canvas_container"),
    contentAlignment = Alignment.Center
  ) {
    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .testTag("npk_radar_canvas")
    ) {
      val centerX = size.width / 2f
      val centerY = size.height / 2f + 6.dp.toPx()
      val maxRadius = (minOf(size.width, size.height) / 2f) - 34.dp.toPx()

      // 3 equilateral axis angles:
      // Nitrogen (N) at -90 degrees (Top)
      // Phosphorus (P) at +150 degrees (Bottom Left)
      // Potassium (K) at +30 degrees (Bottom Right)
      val angleN = Math.toRadians(-90.0)
      val angleP = Math.toRadians(150.0)
      val angleK = Math.toRadians(30.0)

      fun pointAt(angle: Double, fraction: Float): Offset {
        val r = maxRadius * (fraction / 1.4f)
        return Offset(
          (centerX + r * cos(angle)).toFloat(),
          (centerY + r * sin(angle)).toFloat()
        )
      }

      // 1. Draw Concentric Triangle Web Rings (50%, 100% Target, 140%)
      val rings = listOf(0.5f, 1.0f, 1.35f)
      rings.forEach { fraction ->
        val ringPath = Path().apply {
          val p1 = pointAt(angleN, fraction)
          val p2 = pointAt(angleP, fraction)
          val p3 = pointAt(angleK, fraction)
          moveTo(p1.x, p1.y)
          lineTo(p2.x, p2.y)
          lineTo(p3.x, p3.y)
          close()
        }

        if (fraction == 1.0f) {
          // Highlight the 100% target equilibrium ring
          drawPath(
            path = ringPath,
            color = SoilEmerald.copy(alpha = 0.08f),
            style = Fill
          )
          drawPath(
            path = ringPath,
            color = SoilEmerald.copy(alpha = 0.45f),
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
          )
        } else {
          drawPath(
            path = ringPath,
            color = Color(0xFFE2E6DE),
            style = Stroke(width = 1.dp.toPx(), cap = StrokeCap.Round)
          )
        }
      }

      // 2. Draw 3 Radial Axes Spokes from center
      listOf(angleN, angleP, angleK).forEach { angle ->
        val endPoint = pointAt(angle, 1.38f)
        drawLine(
          color = Color(0xFFCBD2C4),
          start = Offset(centerX, centerY),
          end = endPoint,
          strokeWidth = 1.2.dp.toPx()
        )
      }

      // 3. Draw Measured Soil Nutrient Data Polygon
      val ptN = pointAt(angleN, animN)
      val ptP = pointAt(angleP, animP)
      val ptK = pointAt(angleK, animK)

      val dataPath = Path().apply {
        moveTo(ptN.x, ptN.y)
        lineTo(ptP.x, ptP.y)
        lineTo(ptK.x, ptK.y)
        close()
      }

      // Shaded translucent fill
      drawPath(
        path = dataPath,
        brush = Brush.radialGradient(
          colors = listOf(
            SoilEmerald.copy(alpha = 0.38f),
            SoilNitrogenBar.copy(alpha = 0.20f)
          ),
          center = Offset(centerX, centerY),
          radius = maxRadius
        ),
        style = Fill
      )

      // Solid polygon stroke
      drawPath(
        path = dataPath,
        color = SoilForestGreen,
        style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
      )

      // 4. Draw Vertex Node Circles with distinct nutrient colors
      drawCircle(color = Color.White, radius = 7.dp.toPx(), center = ptN)
      drawCircle(color = SoilNitrogenBar, radius = 5.dp.toPx(), center = ptN)

      drawCircle(color = Color.White, radius = 7.dp.toPx(), center = ptP)
      drawCircle(color = SoilPhosphorusBar, radius = 5.dp.toPx(), center = ptP)

      drawCircle(color = Color.White, radius = 7.dp.toPx(), center = ptK)
      drawCircle(color = SoilPotassiumBar, radius = 5.dp.toPx(), center = ptK)
    }

    // Outer Axis Labels (Nitrogen top, Phosphorus bottom-left, Potassium bottom-right)
    Column(
      modifier = Modifier
        .align(Alignment.TopCenter)
        .clickable { onSelectNutrient(SelectedNutrient.NITROGEN) },
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Surface(
        color = if (selectedNutrient == SelectedNutrient.NITROGEN) SoilNitrogenBar else SoilNitrogenLight,
        shape = CircleShape
      ) {
        Text(
          text = "N • ${nitrogen.value.toInt()} mg/kg",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = if (selectedNutrient == SelectedNutrient.NITROGEN) Color.White else SoilNitrogenColor,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
          fontSize = 11.sp
        )
      }
    }

    Column(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .padding(bottom = 6.dp, start = 4.dp)
        .clickable { onSelectNutrient(SelectedNutrient.PHOSPHORUS) },
      horizontalAlignment = Alignment.Start
    ) {
      Surface(
        color = if (selectedNutrient == SelectedNutrient.PHOSPHORUS) SoilPhosphorusBar else SoilPhosphorusLight,
        shape = CircleShape
      ) {
        Text(
          text = "P • ${phosphorus.value.toInt()} mg/kg",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = if (selectedNutrient == SelectedNutrient.PHOSPHORUS) Color.White else SoilPhosphorusColor,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
          fontSize = 11.sp
        )
      }
    }

    Column(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(bottom = 6.dp, end = 4.dp)
        .clickable { onSelectNutrient(SelectedNutrient.POTASSIUM) },
      horizontalAlignment = Alignment.End
    ) {
      Surface(
        color = if (selectedNutrient == SelectedNutrient.POTASSIUM) SoilPotassiumBar else SoilPotassiumLight,
        shape = CircleShape
      ) {
        Text(
          text = "K • ${potassium.value.toInt()} mg/kg",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = if (selectedNutrient == SelectedNutrient.POTASSIUM) Color.White else SoilPotassiumColor,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
          fontSize = 11.sp
        )
      }
    }
  }
}

@Composable
fun NutrientSelectorChip(
  symbol: String,
  label: String,
  valueText: String,
  status: String,
  accentColor: Color,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .clip(RoundedCornerShape(14.dp))
      .clickable(onClick = onClick)
      .testTag("nutrient_chip_$symbol"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) accentColor.copy(alpha = 0.14f) else SoilGreenLight.copy(alpha = 0.5f)
    ),
    border = androidx.compose.foundation.BorderStroke(
      width = if (isSelected) 1.5.dp else 1.dp,
      color = if (isSelected) accentColor else SoilBorderLight
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Box(
          modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(accentColor),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = symbol,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Black,
            color = Color.White,
            fontSize = 10.sp
          )
        }
        Text(
          text = label,
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = SoilTextDark,
          fontSize = 11.sp
        )
      }

      Spacer(modifier = Modifier.height(2.dp))

      Text(
        text = valueText,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Black,
        color = accentColor,
        fontSize = 11.sp
      )
    }
  }
}

@Composable
fun NutrientDetailCard(
  nutrient: NpkNutrientData,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = nutrient.lightColor.copy(alpha = 0.55f)),
    border = androidx.compose.foundation.BorderStroke(1.dp, nutrient.barColor.copy(alpha = 0.35f))
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = if (nutrient.status == "Low") Icons.Default.Warning else Icons.Default.CheckCircle,
            contentDescription = null,
            tint = nutrient.barColor,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "${nutrient.name} (${nutrient.symbol}) Insight",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = SoilForestGreen
          )
        }

        Text(
          text = "Optimal: ${nutrient.targetMin.toInt()}–${nutrient.targetMax.toInt()} ${nutrient.unit}",
          style = MaterialTheme.typography.labelSmall,
          color = SoilTextMuted,
          fontSize = 10.sp
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = nutrient.agronomicRole,
        style = MaterialTheme.typography.bodySmall,
        color = SoilTextDark,
        fontSize = 11.sp,
        lineHeight = 16.sp
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "Action: ${nutrient.recommendation}",
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = SoilForestGreen,
        fontSize = 11.sp,
        lineHeight = 16.sp
      )
    }
  }
}

@Composable
fun ChartModeToggleButton(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(12.dp))
      .background(if (isSelected) SoilForestGreen else Color.Transparent)
      .clickable(onClick = onClick)
      .padding(horizontal = 10.dp, vertical = 5.dp),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (isSelected) Color.White else SoilTextMuted,
        modifier = Modifier.size(14.dp)
      )
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = if (isSelected) Color.White else SoilTextDark,
        fontSize = 11.sp
      )
    }
  }
}

@Composable
fun StatusTag(
  status: String,
  modifier: Modifier = Modifier
) {
  val (bgColor, textColor) = when (status.lowercase()) {
    "optimal", "good" -> SoilGreenLight to SoilEmerald
    "low" -> SoilAmberLight to SoilOrange
    "high" -> Color(0xFFFFF3E0) to SoilOrange
    else -> SoilGreenLight to SoilForestGreen
  }

  Surface(
    color = bgColor,
    shape = CircleShape,
    border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.3f)),
    modifier = modifier
  ) {
    Text(
      text = status.uppercase(),
      style = MaterialTheme.typography.labelSmall,
      fontWeight = FontWeight.Black,
      color = textColor,
      fontSize = 9.sp,
      letterSpacing = 0.5.sp,
      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
    )
  }
}
