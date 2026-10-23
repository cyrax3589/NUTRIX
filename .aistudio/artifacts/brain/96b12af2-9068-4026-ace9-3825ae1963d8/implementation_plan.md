# Complete Application Rename: SoilSmart to NUTRIX

This plan outlines the end-to-end refactoring of the application from SoilSmart to **NUTRIX**, including the Android `applicationId`, full package namespace migration (`com.nutrix`), UI themes, navigation components, dialogs, and test suites, without breaking build stability or live ESP32 cloud telemetry.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> **Confirmed Choices from Phase 1:**
> - **Branding Style**: The user confirmed uppercase **NUTRIX** across all UI headers, dialogs, labels, and copy.
> - **Package Refactoring**: The user selected deep refactoring of all internal package folders and references from `com.example` to `com.nutrix`.
> - **Zero Disruption Guarantee**: The live Render FastAPI + MongoDB Atlas IoT telemetry connection, custom font pairings (Philosopher for NUTRIX branding, Times New Roman for all other text), and automated test suites will remain fully operational.

---

### 1. Overview & Core Concept

- **What It Does**: Unifies the entire mobile application identity under the name **NUTRIX**. Replaces legacy "SoilSmart" naming across system IDs, build scripts, Kotlin packages, UI composables, settings dialogs, and test assertions.
- **Target Audience / Persona**: Soil analysts, agronomists, and smart agriculture operators managing real-time NPK, moisture, temperature, and pH telemetry from ESP32 hardware devices.
- **Key Value**: Delivers consistent, professional branding across the Android OS launcher, notification cards, top app bars, settings dialogues, and deep Android system identifiers.

---

### 2. User Experience & Visual Design

- **Key User Flows**:
  - **Launch & Dashboard**: App opens with top bar displaying **NUTRIX** in bold `Philosopher` font, alongside live sync status to the cloud backend.
  - **Navigation**: Persistent bottom bar navigation remains fluid across Dashboard, Sensors, Analytics, and Settings.
  - **About & Settings**: About dialog updated from "SoilSmart IoT" to **NUTRIX Soil Intelligence Platform**, detailing cloud telemetry features and hardware specs.
- **Visual Identity & Typography**:
  - **Branding Font**: `Philosopher-Bold` and `Philosopher-Regular` strictly for any appearance of **NUTRIX**.
  - **Body / Interface Font**: `Times New Roman` for all sensor readings, metrics, analytical summaries, status indicators, and setting labels.
  - **Color Tokens**: Rich agricultural emerald (`#1B5E20`), vibrant leaf green (`#4CAF50`), soft sage surface tones, and high-contrast dark theme mode.

---

### 3. Key Product Decisions & Trade-Offs

- **Package Migration (`com.example` to `com.nutrix`)**:
  - *Chosen Approach*: Update `namespace` in `app/build.gradle.kts` to `com.nutrix`, update `applicationId` to `com.aistudio.nutrix.app.dxzygm`, relocate source files to `app/src/main/java/com/nutrix/` and `app/src/test/java/com/nutrix/`, and update all `package` and `import` lines.
  - *Why*: Satisfies the user's preference for a complete deep refactor without leftover placeholder package naming.
  - *Safety Measures*: Ensure generated `R` references and `BuildConfig` imports align cleanly before running compilation checks.
- **Navigation & Theme Symbol Renaming**:
  - *Chosen Approach*: Rename `SoilSmartTheme` -> `NutrixTheme`, `SoilSmartApp()` -> `NutrixApp()`, and rename `SoilSmartNav.kt` -> `NutrixNav.kt`.
  - *Why*: Eliminates legacy naming from the developer experience and stack traces while keeping Compose architecture clean.

---

### 4. Technical Architecture & Refactoring Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    Project Configuration                    │
│   • app/build.gradle.kts: namespace = "com.nutrix"          │
│   • applicationId = "com.aistudio.nutrix.app.dxzygm"        │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                   Source Folder Migration                   │
│   • app/src/main/java/com/example/ ──► com/nutrix/          │
│   • app/src/test/java/com/example/ ──► com/nutrix/          │
│   • Update all package headers & imports                    │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                 Component & Symbol Renaming                 │
│   • SoilSmartTheme ──► NutrixTheme (Theme.kt)               │
│   • SoilSmartApp ──► NutrixApp (NutrixNav.kt)               │
│   • MainActivity.kt updated to use NutrixTheme & NutrixApp  │
│   • SettingsScreen.kt dialog copy updated to NUTRIX         │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│               Verification & Telemetry Checks               │
│   • grep check: zero residual soilsmart references          │
│   • compile_applet: Gradle build verification               │
│   • Unit & Screenshot tests updated to com.nutrix           │
└─────────────────────────────────────────────────────────────┘
```

- **Execution Phases**:
  1. **Gradle & Manifest Alignment**: Set `namespace = "com.nutrix"` and `applicationId = "com.aistudio.nutrix.app.dxzygm"` in `app/build.gradle.kts`.
  2. **Directory & Package Refactor**: Move Kotlin source directories from `com/example` to `com/nutrix`. Update all package declarations (`com.nutrix.data`, `com.nutrix.ui`, etc.) and internal imports.
  3. **Theme & Navigation Renaming**: Refactor `SoilSmartTheme` -> `NutrixTheme` and `SoilSmartNav.kt` -> `NutrixNav.kt` with `NutrixApp()`.
  4. **UI Copy & String Resources**: Audit and replace every residual "SoilSmart" string in dialogs, test files, and comments with "NUTRIX".
  5. **Compile & Test Verification**: Run `compile_applet` and verify local unit tests pass cleanly.
