package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R

/**
 * Official NUTRIX circular emblem logo component.
 * Displays the exact user-uploaded `nutrix_logo.png` image asset featuring
 * emerald foliage leaves, organic white negative-space stem contours,
 * layered soil horizon, and integrated NUTRIX typography.
 */
@Composable
fun NutrixLogo(
  modifier: Modifier = Modifier,
  size: Dp = 96.dp,
  elevation: Dp = 4.dp,
  contentDescription: String = "NUTRIX Logo"
) {
  Image(
    painter = painterResource(id = R.drawable.nutrix_logo),
    contentDescription = contentDescription,
    contentScale = ContentScale.Fit,
    modifier = modifier
      .size(size)
      .then(if (elevation > 0.dp) Modifier.shadow(elevation, CircleShape) else Modifier)
      .clip(CircleShape)
      .testTag("nutrix_app_logo")
  )
}
