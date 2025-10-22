package com.gwabs.getpayedpos.domain.sync

import com.gwabs.getpayedpos.data.local.TransactionEntity
import com.gwabs.getpayedpos.network.dto.TransactionDto

fun TransactionEntity.toDto() = TransactionDto(
    id = id,
    amount = amount,
    paymentMethod = paymentMethod.name,
    timestampMillis = timestampMillis,
    latitude = latitude,
    longitude = longitude
)