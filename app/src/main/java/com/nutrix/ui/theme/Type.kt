package com.nutrix.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.nutrix.R

/**
 * Dedicated Philosopher font family for "Nutrix" / "NUTRIX" brand instances.
 */
val PhilosopherFontFamily = FontFamily(
  Font(R.font.philosopher_regular, FontWeight.Normal),
  Font(R.font.philosopher_bold, FontWeight.Bold),
  Font(R.font.philosopher_italic, FontWeight.Normal, FontStyle.Italic)
)

/**
 * Times New Roman font family for everything else in the application.
 */
val TimesNewRomanFontFamily = FontFamily(
  Font(R.font.times_new_roman_regular, FontWeight.Normal),
  Font(R.font.times_new_roman_bold, FontWeight.Bold),
  Font(R.font.times_new_roman_italic, FontWeight.Normal, FontStyle.Italic)
)

/**
 * Helper to format any text string containing "Nutrix" / "NUTRIX",
 * applying Philosopher font to "Nutrix" and Times New Roman font to the rest.
 */
fun formatNutrixText(
  text: String,
  baseStyle: SpanStyle = SpanStyle(fontFamily = TimesNewRomanFontFamily),
  nutrixStyle: SpanStyle = SpanStyle(fontFamily = PhilosopherFontFamily, fontWeight = FontWeight.Bold)
): AnnotatedString {
  val regex = "(?i)nutrix".toRegex()
  var lastIndex = 0
  val matches = regex.findAll(text).toList()
  if (matches.isEmpty()) {
    return buildAnnotatedString {
      withStyle(baseStyle) { append(text) }
    }
  }
  return buildAnnotatedString {
    for (match in matches) {
      if (match.range.first > lastIndex) {
        withStyle(baseStyle) {
          append(text.substring(lastIndex, match.range.first))
        }
      }
      withStyle(nutrixStyle) {
        append(match.value)
      }
      lastIndex = match.range.last + 1
    }
    if (lastIndex < text.length) {
      withStyle(baseStyle) {
        append(text.substring(lastIndex))
      }
    }
  }
}

// Complete Material 3 Typography set configured with Times New Roman as the global default
val Typography =
  Typography(
    displayLarge =
      TextStyle(
        fontFamily = TimesNewRomanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
      ),
    displayMedium =
      TextStyle(
        fontFamily = TimesNewRomanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
      ),
    displaySmall =
      TextStyle(
        fontFamily = TimesNewRomanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
      ),
    headlineLarge =
      TextStyle(
        fontFamily = TimesNewRomanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
      ),
    headlineMedium =
      TextStyle(
        fontFamily = TimesNewRomanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
      ),
    headlineSmall =
      TextStyle(
        fontFamily = TimesNewRomanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
      ),
    titleLarge =
      TextStyle(
        fontFamily = TimesNewRomanFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
      ),
    titleMedium =
      TextStyle(
        fontFamily = TimesNewRomanFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
      ),
    titleSmall =
      TextStyle(
        fontFamily = TimesNewRomanFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
      ),
    bodyLarge =
      TextStyle(
        fontFamily = TimesNewRomanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
      ),
    bodyMedium =
      TextStyle(
        fontFamily = TimesNewRomanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
      ),
    bodySmall =
      TextStyle(
        fontFamily = TimesNewRomanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
      ),
    labelLarge =
      TextStyle(
        fontFamily = TimesNewRomanFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
      ),
    labelMedium =
      TextStyle(
        fontFamily = TimesNewRomanFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
      ),
    labelSmall =
      TextStyle(
        fontFamily = TimesNewRomanFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
      ),
  )
