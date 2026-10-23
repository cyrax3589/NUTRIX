package com.example.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.ui.screens.*
import com.example.ui.theme.*

sealed class Screen(val route: String, val title: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
  object Splash : Screen("splash", "Splash", Icons.Default.Eco, Icons.Outlined.Eco)
  object Login : Screen("login", "Login", Icons.Default.Lock, Icons.Outlined.Lock)
  object Home : Screen("home", "Home", Icons.Default.Home, Icons.Outlined.Home)
  object Test : Screen("test", "Test", Icons.Default.Science, Icons.Outlined.Science)
  object Results : Screen("results", "Results", Icons.Default.Assessment, Icons.Outlined.Assessment)
  object History : Screen("history", "History", Icons.Default.History, Icons.Outlined.History)
  object Recommendations : Screen("recommendations", "AI Tips", Icons.Default.AutoAwesome, Icons.Outlined.AutoAwesome)
  object Calibration : Screen("calibration", "Calibration", Icons.Default.Tune, Icons.Outlined.Tune)
  object SensorHealth : Screen("health", "Health", Icons.Default.Sensors, Icons.Outlined.Sensors)
  object Settings : Screen("settings", "Profile", Icons.Default.Person, Icons.Outlined.Person)
}

val bottomNavItems = listOf(
  Screen.Home,
  Screen.Test,
  Screen.History,
  Screen.Recommendations,
  Screen.Settings
)

@Composable
fun SoilSmartApp() {
  val navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  val showBottomBar = currentRoute !in listOf(Screen.Splash.route, Screen.Login.route)

  Scaffold(
    containerColor = SoilSurfaceCream,
    bottomBar = {
      if (showBottomBar) {
        Surface(
          color = Color.White,
          border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorderLight),
          shadowElevation = 4.dp
        ) {
          NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp,
            modifier = Modifier
              .testTag("bottom_navigation_bar")
          ) {
            bottomNavItems.forEach { screen ->
              val isSelected = currentRoute == screen.route
              NavigationBarItem(
                icon = {
                  Icon(
                    imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                    contentDescription = screen.title,
                    modifier = Modifier.padding(bottom = 2.dp)
                  )
                },
                label = {
                  Text(
                    text = screen.title,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 10.sp
                  )
                },
                selected = isSelected,
                onClick = {
                  if (currentRoute != screen.route) {
                    navController.navigate(screen.route) {
                      popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                      }
                      launchSingleTop = true
                      restoreState = true
                    }
                  }
                },
                colors = NavigationBarItemDefaults.colors(
                  selectedIconColor = SoilEmerald,
                  selectedTextColor = SoilEmerald,
                  indicatorColor = SoilGreenLight,
                  unselectedIconColor = SoilTextMuted,
                  unselectedTextColor = SoilTextMuted
                ),
                modifier = Modifier.testTag("nav_${screen.route}")
              )
            }
          }
        }
      }
    }
  ) { innerPadding ->
    NavHost(
      navController = navController,
      startDestination = Screen.Splash.route,
      modifier = Modifier.padding(innerPadding)
    ) {
      composable(Screen.Splash.route) {
        SplashScreen(
          onNavigateToLogin = {
            navController.navigate(Screen.Login.route) {
              popUpTo(Screen.Splash.route) { inclusive = true }
            }
          }
        )
      }

      composable(Screen.Login.route) {
        LoginScreen(
          onLoginSuccess = {
            navController.navigate(Screen.Home.route) {
              popUpTo(Screen.Login.route) { inclusive = true }
            }
          }
        )
      }

      composable(Screen.Home.route) {
        HomeScreen(
          onNavigateToTest = { navController.navigate(Screen.Test.route) },
          onNavigateToRecommendations = { navController.navigate(Screen.Recommendations.route) },
          onNavigateToCalibration = { navController.navigate(Screen.Calibration.route) },
          onNavigateToSensorHealth = { navController.navigate(Screen.SensorHealth.route) }
        )
      }

      composable(Screen.Test.route) {
        SoilTestScreen(
          onTestComplete = {
            navController.navigate(Screen.Results.route)
          }
        )
      }

      composable(Screen.Results.route) {
        ResultsScreen(
          onNavigateToHistory = { navController.navigate(Screen.History.route) },
          onNavigateToRecommendations = { navController.navigate(Screen.Recommendations.route) }
        )
      }

      composable(Screen.History.route) {
        HistoryScreen()
      }

      composable(Screen.Recommendations.route) {
        RecommendationsScreen()
      }

      composable(Screen.Calibration.route) {
        CalibrationScreen(
          onNavigateBack = { navController.popBackStack() }
        )
      }

      composable(Screen.SensorHealth.route) {
        SensorHealthScreen(
          onNavigateToCalibration = { navController.navigate(Screen.Calibration.route) }
        )
      }

      composable(Screen.Settings.route) {
        SettingsScreen(
          onNavigateToCalibration = { navController.navigate(Screen.Calibration.route) },
          onNavigateToSensorHealth = { navController.navigate(Screen.SensorHealth.route) }
        )
      }
    }
  }
}
