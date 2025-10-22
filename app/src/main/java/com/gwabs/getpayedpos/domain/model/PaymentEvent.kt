package com.gwabs.getpayedpos.domain.model

// domain/model/PaymentEvent.kt
data class PaymentEvent(
    val amount: Double,
    val fromAccountNumber: String,
    val fromAccName: String,
    val fromBankName: String,
    val timestampMillis: Long
)
