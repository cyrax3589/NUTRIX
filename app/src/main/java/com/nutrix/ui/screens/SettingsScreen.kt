package com.nutrix.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nutrix.data.SampleData
import com.nutrix.ui.components.DemoModeBadge
import com.nutrix.ui.components.NutrixLogo
import com.nutrix.ui.components.PrototypeDisclaimerBanner
import com.nutrix.ui.theme.*

@Composable
fun SettingsScreen(
  onNavigateToCalibration: () -> Unit,
  onNavigateToSensorHealth: () -> Unit,
  modifier: Modifier = Modifier
) {
  val profile by SampleData.farmerProfile.collectAsState()
  var isDemoModeOn by remember { mutableStateOf(profile.isDemoMode) }
  var showAboutDialog by remember { mutableStateOf(false) }
  var showLanguageDialog by remember { mutableStateOf(false) }
  var showUnitsDialog by remember { mutableStateOf(false) }
  var showProfileDialog by remember { mutableStateOf(false) }

  Surface(
    modifier = modifier
      .fillMaxSize()
      .testTag("settings_screen"),
    color = SoilSurfaceCream
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Settings & Profile",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            color = SoilForestGreen
          )
          Text(
            text = "System Preferences & Device",
            style = MaterialTheme.typography.bodySmall,
            color = SoilTextMuted
          )
        }
        DemoModeBadge()
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Profile Summary Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { showProfileDialog = true }
          .testTag("profile_summary_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SoilCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorder)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(SoilGreenLight),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = "Farmer Profile",
              tint = SoilForestGreen,
              modifier = Modifier.size(30.dp)
            )
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = profile.name,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = SoilTextDark
            )
            Text(
              text = "${profile.farmName} • ${profile.farmSizeAcres} Acres",
              style = MaterialTheme.typography.bodySmall,
              color = SoilTextMuted
            )
            Text(
              text = profile.phone,
              style = MaterialTheme.typography.labelSmall,
              color = SoilEmerald,
              fontWeight = FontWeight.SemiBold
            )
          }

          Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit Profile",
            tint = SoilTextMuted,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // DEMO MODE TOGGLE CARD
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SoilAmberLight),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoilAmber.copy(alpha = 0.5f))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color.White),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Outlined.Science,
                contentDescription = null,
                tint = SoilOrange,
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Demo Mode",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SoilDarkBrown
              )
              Text(
                text = "Runs complete prototype without physical BLE hardware",
                style = MaterialTheme.typography.bodySmall,
                color = SoilTextMuted,
                lineHeight = 16.sp
              )
            }
          }

          Switch(
            checked = isDemoModeOn,
            onCheckedChange = {
              isDemoModeOn = it
              SampleData.updateProfile(profile.copy(isDemoMode = it))
            },
            colors = SwitchDefaults.colors(
              checkedThumbColor = Color.White,
              checkedTrackColor = SoilForestGreen
            ),
            modifier = Modifier.testTag("demo_mode_switch")
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // SETTINGS GROUP: APP & SENSOR PREFERENCES
      Text(
        text = "CONFIGURATION & SENSORS",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = SoilForestGreen,
        letterSpacing = 0.5.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SoilCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorder)
      ) {
        Column {
          SettingsClickableRow(
            icon = Icons.Outlined.Language,
            title = "Language",
            subtitle = profile.selectedLanguage,
            onClick = { showLanguageDialog = true }
          )
          HorizontalDivider(color = SoilBorder)

          SettingsClickableRow(
            icon = Icons.Outlined.Straighten,
            title = "Units",
            subtitle = profile.selectedUnit,
            onClick = { showUnitsDialog = true }
          )
          HorizontalDivider(color = SoilBorder)

          SettingsClickableRow(
            icon = Icons.Outlined.Sensors,
            title = "Connected Device",
            subtitle = "NUTRIX ISE-Probe V1 (BLE #4829)",
            onClick = onNavigateToSensorHealth
          )
          HorizontalDivider(color = SoilBorder)

          SettingsClickableRow(
            icon = Icons.Outlined.Tune,
            title = "Sensor Calibration",
            subtitle = "Potassium & Nitrate standard points",
            onClick = onNavigateToCalibration
          )
          HorizontalDivider(color = SoilBorder)

          SettingsClickableRow(
            icon = Icons.Outlined.Notifications,
            title = "Notification Settings",
            subtitle = "Field testing & calibration reminders (Enabled)",
            onClick = {}
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // SETTINGS GROUP: ABOUT & PROJECT INFO
      Text(
        text = "PROJECT & ABOUT",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = SoilForestGreen,
        letterSpacing = 0.5.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SoilCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorder)
      ) {
        Column {
          SettingsClickableRow(
            icon = Icons.Outlined.Info,
            title = "About NUTRIX",
            subtitle = "Nutrient Monitoring & Intelligence Exchange",
            onClick = { showAboutDialog = true }
          )
          HorizontalDivider(color = SoilBorder)

          SettingsClickableRow(
            icon = Icons.Outlined.HelpOutline,
            title = "Help & User Guide",
            subtitle = "How to prepare soil suspensions & ISE sensors",
            onClick = { showAboutDialog = true }
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
      PrototypeDisclaimerBanner()
    }
  }

  // Language Dialog
  if (showLanguageDialog) {
    AlertDialog(
      onDismissRequest = { showLanguageDialog = false },
      title = { Text("Select Language", fontWeight = FontWeight.Bold) },
      text = {
        Column {
          listOf("English", "Hindi (हिंदी)", "Kannada (ಕನ್ನಡ)", "Tamil (தமிழ்)", "Telugu (తెలుగు)").forEach { lang ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  SampleData.updateProfile(profile.copy(selectedLanguage = lang.substringBefore(" (")))
                  showLanguageDialog = false
                }
                .padding(vertical = 12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = profile.selectedLanguage == lang.substringBefore(" ("),
                onClick = {
                  SampleData.updateProfile(profile.copy(selectedLanguage = lang.substringBefore(" (")))
                  showLanguageDialog = false
                }
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(lang, style = MaterialTheme.typography.bodyMedium)
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showLanguageDialog = false }) { Text("Close") }
      }
    )
  }

  // Units Dialog
  if (showUnitsDialog) {
    AlertDialog(
      onDismissRequest = { showUnitsDialog = false },
      title = { Text("Nutrient Measurement Units", fontWeight = FontWeight.Bold) },
      text = {
        Column {
          listOf("Standard (mg/kg, mM)", "Parts Per Million (ppm)", "Agronomic Index (kg/ha)").forEach { unit ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  SampleData.updateProfile(profile.copy(selectedUnit = unit))
                  showUnitsDialog = false
                }
                .padding(vertical = 12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = profile.selectedUnit == unit,
                onClick = {
                  SampleData.updateProfile(profile.copy(selectedUnit = unit))
                  showUnitsDialog = false
                }
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(unit, style = MaterialTheme.typography.bodyMedium)
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showUnitsDialog = false }) { Text("Close") }
      }
    )
  }

  // Profile Edit Dialog
  if (showProfileDialog) {
    var editedName by remember(profile.name) { mutableStateOf(profile.name) }
    var editedFarmName by remember(profile.farmName) { mutableStateOf(profile.farmName) }
    var editedPhone by remember(profile.phone) { mutableStateOf(profile.phone) }
    var editedLocation by remember(profile.farmLocation) { mutableStateOf(profile.farmLocation) }

    AlertDialog(
      onDismissRequest = { showProfileDialog = false },
      title = {
        Text(
          text = "Edit Farmer Profile",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = SoilForestGreen
        )
      },
      text = {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedTextField(
            value = editedName,
            onValueChange = { editedName = it },
            label = { Text("User Name") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
          )
          OutlinedTextField(
            value = editedFarmName,
            onValueChange = { editedFarmName = it },
            label = { Text("Farm Name") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = editedPhone,
            onValueChange = { editedPhone = it },
            label = { Text("Phone Number") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = editedLocation,
            onValueChange = { editedLocation = it },
            label = { Text("Location") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            SampleData.updateProfile(
              profile.copy(
                name = editedName.ifBlank { "Cyrax" },
                farmName = editedFarmName,
                phone = editedPhone,
                farmLocation = editedLocation
              )
            )
            showProfileDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = SoilForestGreen)
        ) {
          Text("Save")
        }
      },
      dismissButton = {
        TextButton(onClick = { showProfileDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }

  // About NUTRIX Project Dialog
  if (showAboutDialog) {
    Dialog(onDismissRequest = { showAboutDialog = false }) {
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SoilCardBg),
        modifier = Modifier.fillMaxWidth().testTag("about_dialog")
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(22.dp)
            .verticalScroll(rememberScrollState())
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            NutrixLogo(size = 64.dp, elevation = 3.dp)
            Column {
              Text(
                text = "NUTRIX",
                fontFamily = PhilosopherFontFamily,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = SoilForestGreen
              )
              Text(
                text = "Intelligence Exchange",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = SoilEmerald
              )
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "“Nutrient Monitoring & Intelligence Exchange System”",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = SoilEmerald
          )

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "AIoT-Based Soil Analysis Prototype:\n“Smart Soil Nutrients Analyzer & Recommendation System”",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = SoilTextDark
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Key Innovation & Physical Concept:\n• Ion-Selective Electrodes (ISEs) for Potassium (K⁺) and Nitrate (NO₃⁻)\n• Potentiometric voltage acquisition via high-precision ADC\n• Cellulose / filter-paper substrate with capillary-assisted sampling\n• Smartphone-based 2-point Nernstian calibration\n• Agricultural AI crop recommendation engine",
            style = MaterialTheme.typography.bodySmall,
            color = SoilTextDark,
            lineHeight = 18.sp
          )

          Spacer(modifier = Modifier.height(16.dp))

          Button(
            onClick = { showAboutDialog = false },
            colors = ButtonDefaults.buttonColors(containerColor = SoilForestGreen),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Got it")
          }
        }
      }
    }
  }
}

@Composable
fun SettingsClickableRow(
  icon: ImageVector,
  title: String,
  subtitle: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(horizontal = 16.dp, vertical = 14.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      modifier = Modifier.weight(1f),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(38.dp)
          .clip(CircleShape)
          .background(SoilGreenLight),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = SoilForestGreen,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = formatNutrixText(title),
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.SemiBold,
          color = SoilTextDark
        )
        Text(
          text = formatNutrixText(subtitle),
          style = MaterialTheme.typography.bodySmall,
          color = SoilTextMuted
        )
      }
    }

    Icon(
      imageVector = Icons.Default.ChevronRight,
      contentDescription = null,
      tint = SoilTextMuted,
      modifier = Modifier.size(18.dp)
    )
  }
}
