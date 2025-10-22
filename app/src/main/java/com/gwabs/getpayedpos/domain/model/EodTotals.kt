package com.gwabs.getpayedpos.domain.model

data class EodTotals(
    val count: Int,
    val sum: Double
)

data class EodReport(
    val dayStartMillis: Long,
    val dayEndMillis: Long,
    val cash: EodTotals,
    val card: EodTotals,
    val pendingCount: Int,
    val pendingSum: Double,
    val syncedCount: Int,
    val syncedSum: Double,
    val incomingTransfersSum: Double,
    val startingBalance: Double,
    val endingBalance: Double
)
