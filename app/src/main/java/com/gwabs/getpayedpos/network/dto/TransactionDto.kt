package com.gwabs.getpayedpos.network.dto

data class TransactionDto(
    val id: Long,            // local id (client-generated)
    val amount: Double,
    val paymentMethod: String, // "CASH" | "CARD"
    val timestampMillis: Long,
    val latitude: Double?,
    val longitude: Double?
)

data class SyncRequest(val transactions: List<TransactionDto>)
data class SyncResponse(val syncedIds: List<Long>)