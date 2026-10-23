package com.nutrix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nutrix.ui.navigation.NutrixApp
import com.nutrix.ui.theme.NutrixTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      NutrixTheme {
        NutrixApp()
      }
    }
  }
}

