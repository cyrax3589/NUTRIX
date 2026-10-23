package com.nutrix.model

data class SoilTestRecord(
  val id: String,
  val date: String,
  val time: String = "10:30 AM",
  val fieldName: String,
  val potassiumMgKg: Double, // Target: 50 - 80 mg/kg
  val nitrateMm: Double,     // Target: 0.25 - 0.45 mM
  val healthScore: Double,   // 0.0 - 10.0
  val status: String,        // "Good", "Optimal", "Needs Attention"
  val readingStatus: String = "Stable",
  val calibrationStatus: String = "Calibrated",
  val phLevel: Double = 6.8,
  val moisturePercent: Int = 34,
  val notes: String = "Capillary ISE potentiometric field test",
  val phosphorusMgKg: Double = 24.0, // Target: 15 - 30 mg/kg (Available P₂O₅)
  val nitrogenMgKg: Double = 208.0   // Target: 180 - 280 mg/kg (Available Nitrogen)
) {
  val nitrogenStatus: String
    get() = when {
      nitrogenMgKg < 180.0 -> "Low"
      nitrogenMgKg <= 280.0 -> "Optimal"
      else -> "High"
    }

  val phosphorusStatus: String
    get() = when {
      phosphorusMgKg < 15.0 -> "Low"
      phosphorusMgKg <= 30.0 -> "Optimal"
      else -> "High"
    }

  val potassiumStatus: String
    get() = when {
      potassiumMgKg < 50.0 -> "Low"
      potassiumMgKg <= 80.0 -> "Optimal"
      else -> "High"
    }

  // N:P:K Ratio formatted string (normalized to P = 1.0)
  val npkRatioText: String
    get() {
      val pRef = if (phosphorusMgKg > 0.0) phosphorusMgKg else 1.0
      val nRatio = String.format(java.util.Locale.US, "%.1f", (nitrogenMgKg / pRef).coerceAtLeast(0.1))
      val kRatio = String.format(java.util.Locale.US, "%.1f", (potassiumMgKg / pRef).coerceAtLeast(0.1))
      return "$nRatio : 1.0 : $kRatio"
    }
}

data class CropRecommendation(
  val id: String,
  val name: String,
  val regionalName: String,
  val suitabilityPercent: Int,
  val soilType: String,
  val nitrogenNeed: String,
  val potassiumNeed: String,
  val yieldExpectation: String,
  val waterRequirement: String,
  val growthDuration: String,
  val guidanceNote: String
)

data class SensorCalibrationPoint(
  val standardSolution: String,
  val measuredMv: Double,
  val isRecorded: Boolean = true
)

data class SensorCalibrationState(
  val potassiumPoint1: SensorCalibrationPoint = SensorCalibrationPoint("1 mM Standard (K⁺)", 12.4),
  val potassiumPoint2: SensorCalibrationPoint = SensorCalibrationPoint("10 mM Standard (K⁺)", 64.1),
  val nitratePoint1: SensorCalibrationPoint = SensorCalibrationPoint("0.1 mM Standard (NO₃⁻)", -18.2),
  val nitratePoint2: SensorCalibrationPoint = SensorCalibrationPoint("1.0 mM Standard (NO₃⁻)", -74.5),
  val isCalibrated: Boolean = true,
  val lastCalibratedDate: String = "02 Sep 2026, 09:30 AM",
  val slopeKmVDilution: Double = 51.7, // mV/decade
  val slopeNo3mVDilution: Double = -56.3, // mV/decade
  val testsPerformedCount: Int = 12,
  val stabilityPercent: Int = 98,
  val electrodeCondition: String = "Healthy (Cellulose Matrix Intact)",
  val isWarningTriggered: Boolean = false
)

data class FarmerProfile(
  val name: String = "Cyrax",
  val phone: String = "+91 98450 12345",
  val farmLocation: String = "Bengaluru, Karnataka",
  val farmName: String = "Green Acres Farm",
  val farmSizeAcres: Double = 4.5,
  val currentCrop: String = "Rice (Paddy)",
  val soilType: String = "Loamy Clay",
  val selectedLanguage: String = "English",
  val selectedUnit: String = "Standard (mg/kg, mM)",
  val isDemoMode: Boolean = true
)
