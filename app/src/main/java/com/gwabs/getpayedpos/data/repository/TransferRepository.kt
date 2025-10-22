package com.gwabs.getpayedpos.data.repository

import com.gwabs.getpayedpos.data.local.IncomingTransferEntity
import com.gwabs.getpayedpos.data.local.dao.IncomingTransferDao

interface TransferRepository {

    suspend fun logIncoming(amount: Double, fromAccountNumber: String,fromAccName: String,fromBankName: String, ts: Long = System.currentTimeMillis()): Long
    suspend fun getInRange(start: Long, end: Long): List<IncomingTransferEntity>
}

class TransferRepositoryImpl(
    private val dao: IncomingTransferDao
) : TransferRepository {
    override suspend fun logIncoming(amount: Double, fromAccountNumber: String,fromAccName: String,fromBankName: String, ts: Long): Long =
        dao.insert(IncomingTransferEntity(amount = amount, fromAccountNumber = fromAccountNumber, fromAccName = fromAccName, fromBankName = fromBankName, timestampMillis = ts))

    override suspend fun getInRange(start: Long, end: Long): List<IncomingTransferEntity> =
        dao.getInRange(start, end)
}
