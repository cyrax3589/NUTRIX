package com.nutrix.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nutrix.data.SampleData
import com.nutrix.ui.components.DemoModeBadge
import com.nutrix.ui.components.NutrixLogo
import com.nutrix.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
  onLoginSuccess: () -> Unit,
  modifier: Modifier = Modifier
) {
  var phoneNumber by remember { mutableStateOf("9845012345") }
  var selectedLanguage by remember { mutableStateOf("English") }
  var isLangDropdownExpanded by remember { mutableStateOf(false) }

  val languages = listOf("English", "Hindi (हिंदी)", "Kannada (ಕನ್ನಡ)", "Tamil (தமிழ்)", "Telugu (తెలుగు)")

  Surface(
    modifier = modifier
      .fillMaxSize()
      .testTag("login_screen"),
    color = SoilSurfaceCream
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(12.dp))

      DemoModeBadge()

      Spacer(modifier = Modifier.height(28.dp))

      // Official Nutrix Logo Emblem
      NutrixLogo(
        size = 86.dp,
        elevation = 6.dp
      )

      Spacer(modifier = Modifier.height(18.dp))

      Text(
        text = formatNutrixText("Welcome to NUTRIX"),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = SoilForestGreen
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Nutrient Monitoring & Intelligence Exchange System",
        style = MaterialTheme.typography.bodyMedium,
        color = SoilTextMuted,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(32.dp))

      // Card containing the login options
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SoilCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoilBorder)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          // Language Selection Header
          Text(
            text = "Select Language / ಭಾಷೆ / भाषा",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = SoilForestGreen
          )

          Spacer(modifier = Modifier.height(8.dp))

          ExposedDropdownMenuBox(
            expanded = isLangDropdownExpanded,
            onExpandedChange = { isLangDropdownExpanded = !isLangDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
          ) {
            OutlinedTextField(
              value = selectedLanguage,
              onValueChange = {},
              readOnly = true,
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isLangDropdownExpanded) },
              leadingIcon = {
                Icon(
                  imageVector = Icons.Default.Language,
                  contentDescription = "Language",
                  tint = SoilForestGreen
                )
              },
              shape = RoundedCornerShape(14.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SoilForestGreen,
                unfocusedBorderColor = SoilBorder,
                focusedContainerColor = SoilGreenSurface,
                unfocusedContainerColor = SoilSurfaceCream
              ),
              modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .testTag("language_selector")
            )

            ExposedDropdownMenu(
              expanded = isLangDropdownExpanded,
              onDismissRequest = { isLangDropdownExpanded = false }
            ) {
              languages.forEach { lang ->
                DropdownMenuItem(
                  text = {
                    Text(
                      text = lang,
                      fontWeight = if (lang.startsWith(selectedLanguage)) FontWeight.Bold else FontWeight.Normal
                    )
                  },
                  onClick = {
                    selectedLanguage = lang.substringBefore(" (")
                    isLangDropdownExpanded = false
                  }
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          // Phone Number Field
          Text(
            text = "Mobile Number",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = SoilForestGreen
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            leadingIcon = {
              Row(
                modifier = Modifier.padding(start = 12.dp, end = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "+91",
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Bold,
                  color = SoilForestGreen
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                  modifier = Modifier
                    .width(1.dp)
                    .height(20.dp)
                    .background(SoilBorder)
                )
              }
            },
            placeholder = { Text("Enter 10-digit number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = SoilForestGreen,
              unfocusedBorderColor = SoilBorder,
              focusedContainerColor = SoilGreenSurface,
              unfocusedContainerColor = SoilSurfaceCream
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("phone_number_input")
          )

          Spacer(modifier = Modifier.height(24.dp))

          // Primary Continue Button
          Button(
            onClick = {
              SampleData.updateProfile(
                SampleData.farmerProfile.value.copy(
                  phone = "+91 $phoneNumber",
                  selectedLanguage = selectedLanguage
                )
              )
              onLoginSuccess()
            },
            colors = ButtonDefaults.buttonColors(containerColor = SoilForestGreen),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(54.dp)
              .testTag("continue_btn")
          ) {
            Text(
              text = "Continue",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Continue as Guest Button
          OutlinedButton(
            onClick = onLoginSuccess,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = SoilForestGreen),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, SoilForestGreen),
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .testTag("guest_login_btn")
          ) {
            Icon(imageVector = Icons.Default.PersonOutline, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Continue as Guest",
              style = MaterialTheme.typography.bodyLarge,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      Text(
        text = "No actual OTP required for demonstration prototype",
        style = MaterialTheme.typography.labelSmall,
        color = SoilTextMuted,
        fontSize = 11.sp
      )
    }
  }
}
