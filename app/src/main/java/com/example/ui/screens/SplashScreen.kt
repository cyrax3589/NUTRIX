package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DemoModeBadge
import com.example.ui.components.NutrixLogo
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  onNavigateToLogin: () -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.95f,
    targetValue = 1.06f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_scale"
  )

  LaunchedEffect(Unit) {
    delay(2400) // gentle splash delay for showcase
    onNavigateToLogin()
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        brush = Brush.verticalGradient(
          colors = listOf(
            SoilForestGreen,
            SoilEmerald,
            Color(0xFF163E1B)
          )
        )
      )
      .padding(24.dp)
      .testTag("splash_screen"),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.fillMaxWidth()
    ) {
      DemoModeBadge(
        modifier = Modifier.padding(bottom = 24.dp)
      )

      // Sprout & ISE Sensor Ring Graphic
      Box(
        modifier = Modifier
          .size(160.dp)
          .scale(pulseScale),
        contentAlignment = Alignment.Center
      ) {
        // Outer concentric sensor rings
        Box(
          modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.08f))
        )
        Box(
          modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.15f))
        )
        Box(
          modifier = Modifier
            .size(126.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.12f)),
          contentAlignment = Alignment.Center
        ) {
          NutrixLogo(
            size = 112.dp,
            elevation = 6.dp
          )
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      Text(
        text = "NUTRIX",
        fontFamily = PhilosopherFontFamily,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        letterSpacing = 2.5.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Nutrient Monitoring & Intelligence Exchange System",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Medium,
        color = SoilGreenMint,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(16.dp))

      Surface(
        color = Color.White.copy(alpha = 0.12f),
        shape = RoundedCornerShape(16.dp)
      ) {
        Text(
          text = "AIoT-Based Soil Analysis\nPotassium (K⁺) • Nitrate (NO₃⁻)",
          style = MaterialTheme.typography.bodySmall,
          color = Color.White.copy(alpha = 0.85f),
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
      }

      Spacer(modifier = Modifier.height(48.dp))

      // Direct Action Button to start or skip splash
      Button(
        onClick = onNavigateToLogin,
        colors = ButtonDefaults.buttonColors(
          containerColor = SoilGreenLight,
          contentColor = SoilForestGreen
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .fillMaxWidth(0.8f)
          .height(52.dp)
          .testTag("splash_get_started_btn")
      ) {
        Text(
          text = "Get Started",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
          imageVector = Icons.Default.ArrowForward,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )
      }
    }

    // Footer Version note
    Column(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Final Year Engineering Prototype v1.0",
        style = MaterialTheme.typography.labelSmall,
        color = Color.White.copy(alpha = 0.6f)
      )
    }
  }
}
