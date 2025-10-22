package com.gwabs.getpayedpos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val amount: Double,
    val paymentMethod: PaymentMethod,
    val timestampMillis: Long,
    val status: SyncStatus,             // PENDING or SYNCED
    val latitude: Double?,
    val longitude: Double?
)
enum class PaymentMethod { CASH, CARD,TRANSFER }
enum class SyncStatus { PENDING, SYNCED }
