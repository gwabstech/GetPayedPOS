package com.gwabs.getpayedpos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incoming_transfers")
data class IncomingTransferEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val amount: Double,
    val fromAccountNumber: String,
    val fromAccName: String,
    val fromBankName: String,
    val timestampMillis: Long
)
