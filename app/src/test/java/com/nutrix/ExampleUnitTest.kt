package com.nutrix

import com.nutrix.model.SoilTestRecord
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testSoilTestRecord_npkStatusAndRatio() {
    val record = SoilTestRecord(
      id = "TEST-01",
      date = "02 Sep 2026",
      fieldName = "Test Plot",
      potassiumMgKg = 62.0,
      nitrateMm = 0.32,
      healthScore = 8.2,
      status = "Good",
      phosphorusMgKg = 24.0,
      nitrogenMgKg = 210.0
    )

    assertEquals("Optimal", record.nitrogenStatus)
    assertEquals("Optimal", record.phosphorusStatus)
    assertEquals("Optimal", record.potassiumStatus)
    assertTrue(record.npkRatioText.contains("1.0"))
  }
}
