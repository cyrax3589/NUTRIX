package com.nutrix.data

import com.nutrix.model.CropRecommendation
import com.nutrix.model.FarmerProfile
import com.nutrix.model.SensorCalibrationState
import com.nutrix.model.SoilTestRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object SampleData {

  val defaultLatestTest = SoilTestRecord(
    id = "TEST-2026-0902-1",
    date = "02 Sep 2026",
    time = "09:45 AM",
    fieldName = "Demo Field",
    potassiumMgKg = 62.0,
    nitrateMm = 0.32,
    healthScore = 8.2,
    status = "Good",
    readingStatus = "Stable",
    calibrationStatus = "Calibrated",
    phLevel = 6.8,
    moisturePercent = 38,
    notes = "Optimal K⁺ and NO₃⁻ balance recorded with Cellulose ISE sensor.",
    phosphorusMgKg = 24.0,
    nitrogenMgKg = 210.0
  )

  val initialHistoryList = listOf(
    defaultLatestTest,
    SoilTestRecord(
      id = "TEST-2026-0825-2",
      date = "25 Aug 2026",
      time = "11:15 AM",
      fieldName = "North Field",
      potassiumMgKg = 54.0,
      nitrateMm = 0.28,
      healthScore = 7.8,
      status = "Good",
      readingStatus = "Stable",
      calibrationStatus = "Calibrated",
      phLevel = 6.6,
      moisturePercent = 32,
      notes = "Pre-sowing nutrient baseline check.",
      phosphorusMgKg = 21.0,
      nitrogenMgKg = 185.0
    ),
    SoilTestRecord(
      id = "TEST-2026-0818-3",
      date = "18 Aug 2026",
      time = "04:30 PM",
      fieldName = "South Field",
      potassiumMgKg = 47.0,
      nitrateMm = 0.19,
      healthScore = 5.4,
      status = "Needs Attention",
      readingStatus = "Stable",
      calibrationStatus = "Calibrated",
      phLevel = 6.2,
      moisturePercent = 25,
      notes = "Nitrate level slightly depleted; recommended organic manure compost.",
      phosphorusMgKg = 12.0,
      nitrogenMgKg = 125.0
    ),
    SoilTestRecord(
      id = "TEST-2026-0804-4",
      date = "04 Aug 2026",
      time = "08:20 AM",
      fieldName = "East Orchard",
      potassiumMgKg = 70.0,
      nitrateMm = 0.38,
      healthScore = 8.9,
      status = "Optimal",
      readingStatus = "Stable",
      calibrationStatus = "Calibrated",
      phLevel = 7.0,
      moisturePercent = 42,
      notes = "Rich vegetative bed condition for horticultural crops.",
      phosphorusMgKg = 28.0,
      nitrogenMgKg = 252.0
    )
  )

  val cropRecommendations = listOf(
    CropRecommendation(
      id = "crop-rice",
      name = "Rice",
      regionalName = "Nellu / Akki (ಭತ್ತ / நெல்)",
      suitabilityPercent = 92,
      soilType = "Loamy Clay / Wetland",
      nitrogenNeed = "Moderate to High (Adequate NO₃⁻: 0.32 mM)",
      potassiumNeed = "High (Optimal K⁺: 62 mg/kg)",
      yieldExpectation = "4.8 - 5.2 tonnes / hectare",
      waterRequirement = "High (Standing water during tillering)",
      growthDuration = "110 - 130 Days",
      guidanceNote = "Current soil potassium level (62 mg/kg) matches grain-filling requirement. Nitrogen is adequate for vegetative development."
    ),
    CropRecommendation(
      id = "crop-ragi",
      name = "Ragi (Finger Millet)",
      regionalName = "Mandua / Kelvaragu (ರಾಗಿ / கேழ்வரகு)",
      suitabilityPercent = 84,
      soilType = "Red Loamy / Well Drained",
      nitrogenNeed = "Low to Medium",
      potassiumNeed = "Moderate",
      yieldExpectation = "2.8 - 3.2 tonnes / hectare",
      waterRequirement = "Low to Drought Tolerant",
      growthDuration = "95 - 105 Days",
      guidanceNote = "Highly suitable for local agro-climatic conditions. Minimal external fertilizer required due to resilient root assimilation."
    ),
    CropRecommendation(
      id = "crop-sugarcane",
      name = "Sugarcane",
      regionalName = "Karumbu / Kabbu (ಕಬ್ಬು / கரும்பு)",
      suitabilityPercent = 78,
      soilType = "Deep Well-aerated Loam",
      nitrogenNeed = "High (Slight booster may be required)",
      potassiumNeed = "Very High",
      yieldExpectation = "90 - 110 tonnes / hectare",
      waterRequirement = "Very High (Furrow irrigation)",
      growthDuration = "10 - 12 Months",
      guidanceNote = "High potassium promotes sugar translocation. Monitor periodic nitrate depletion during grand growth phase."
    ),
    CropRecommendation(
      id = "crop-cotton",
      name = "Cotton",
      regionalName = "Hatti / Kapas (ಹತ್ತಿ / பருத்தி)",
      suitabilityPercent = 71,
      soilType = "Black / Mixed Loam",
      nitrogenNeed = "Moderate",
      potassiumNeed = "High for boll weight",
      yieldExpectation = "2.2 - 2.5 tonnes / hectare",
      waterRequirement = "Moderate (Drip recommended)",
      growthDuration = "150 - 160 Days",
      guidanceNote = "Adequate drainage required. Potassium level supports good boll formation and fiber strength."
    )
  )

  // In-memory application state holder for demo flow
  private val _currentLatestTest = MutableStateFlow(defaultLatestTest)
  val currentLatestTest = _currentLatestTest.asStateFlow()

  private val _historyList = MutableStateFlow(initialHistoryList)
  val historyList = _historyList.asStateFlow()

  private val _calibrationState = MutableStateFlow(SensorCalibrationState())
  val calibrationState = _calibrationState.asStateFlow()

  private val _farmerProfile = MutableStateFlow(FarmerProfile())
  val farmerProfile = _farmerProfile.asStateFlow()

  // Cloud backend repository for Render FastAPI + MongoDB Atlas live readings
  val cloudRepository: com.nutrix.data.cloud.NutrixCloudRepository = com.nutrix.data.cloud.NutrixCloudRepository()

  fun updateLatestTest(newTest: SoilTestRecord) {
    _currentLatestTest.value = newTest
    _historyList.value = listOf(newTest) + _historyList.value
  }

  fun updateCalibration(state: SensorCalibrationState) {
    _calibrationState.value = state
  }

  fun toggleWarningSimulation() {
    val current = _calibrationState.value
    _calibrationState.value = current.copy(
      isWarningTriggered = !current.isWarningTriggered,
      stabilityPercent = if (!current.isWarningTriggered) 74 else 98
    )
  }

  fun resetCalibration() {
    _calibrationState.value = SensorCalibrationState(
      isWarningTriggered = false,
      stabilityPercent = 98,
      lastCalibratedDate = "02 Sep 2026, 09:30 AM"
    )
  }

  fun updateProfile(profile: FarmerProfile) {
    _farmerProfile.value = profile
  }
}
