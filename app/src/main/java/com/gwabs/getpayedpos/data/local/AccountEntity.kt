package com.gwabs.getpayedpos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey val id: Int = 1,
    val accountNumber: String,
    val accountName: String,
    val bankName: String,
    val balance: Double
)
