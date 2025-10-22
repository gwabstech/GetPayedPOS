package com.gwabs.getpayedpos.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun toPaymentMethod(name: String): PaymentMethod = PaymentMethod.valueOf(name)

    @TypeConverter
    fun fromPaymentMethod(method: PaymentMethod): String = method.name

    @TypeConverter
    fun toSyncStatus(name: String): SyncStatus = SyncStatus.valueOf(name)

    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String = status.name
}