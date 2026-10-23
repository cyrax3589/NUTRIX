package com.nutrix.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
  primary = SoilForestGreen,
  onPrimary = SoilCardBg,
  primaryContainer = SoilGreenLight,
  onPrimaryContainer = SoilForestGreen,
  secondary = SoilEmerald,
  onSecondary = SoilCardBg,
  secondaryContainer = SoilGreenMint,
  onSecondaryContainer = SoilForestGreen,
  tertiary = SoilPotassiumColor,
  onTertiary = SoilCardBg,
  tertiaryContainer = SoilPotassiumLight,
  onTertiaryContainer = SoilPotassiumColor,
  background = SoilSurfaceCream,
  onBackground = SoilTextDark,
  surface = SoilCardBg,
  onSurface = SoilTextDark,
  surfaceVariant = SoilGreenSurface,
  onSurfaceVariant = SoilTextMedium,
  outline = SoilBorder,
  error = SoilRed,
  errorContainer = SoilRedLight,
  onError = SoilCardBg
)

private val DarkColorScheme = darkColorScheme(
  primary = SoilGreenAccent,
  onPrimary = SoilTextDark,
  primaryContainer = SoilForestGreen,
  onPrimaryContainer = SoilGreenMint,
  secondary = SoilGreenMint,
  onSecondary = SoilTextDark,
  background = SoilTextDark,
  onBackground = SoilSurfaceCream,
  surface = Color(0xFF1E2B20),
  onSurface = SoilSurfaceCream
)

@Composable
fun NutrixTheme(
  darkTheme: Boolean = false, // Default to light agricultural theme for farmers
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as? Activity)?.window
      if (window != null) {
        window.statusBarColor = colorScheme.background.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
      }
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
