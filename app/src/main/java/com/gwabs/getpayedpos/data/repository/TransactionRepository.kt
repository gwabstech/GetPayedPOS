package com.gwabs.getpayedpos.data.repository

import com.gwabs.getpayedpos.data.local.PaymentMethod
import com.gwabs.getpayedpos.data.local.SyncStatus
import com.gwabs.getpayedpos.data.local.TransactionEntity
import com.gwabs.getpayedpos.data.local.dao.TransactionDao
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun transactions(): Flow<List<TransactionEntity>>
    suspend fun addTransaction(
        amount: Double,
        paymentMethod: PaymentMethod,
        latitude: Double?,
        longitude: Double?,
        timestampMillis: Long = System.currentTimeMillis()
    ): Long
    suspend fun pending(): List<TransactionEntity>
    suspend fun markSynced(ids: List<Long>)
    suspend fun getInRange(start: Long, end: Long): List<TransactionEntity>
}

class TransactionRepositoryImpl(
    private val dao: TransactionDao
) : TransactionRepository {
    override fun transactions(): Flow<List<TransactionEntity>> = dao.observeAll()
    override suspend fun addTransaction(
        amount: Double, paymentMethod: PaymentMethod,
        latitude: Double?, longitude: Double?, timestampMillis: Long
    ): Long = dao.insert(
        TransactionEntity(
            amount = amount,
            paymentMethod = paymentMethod,
            timestampMillis = timestampMillis,
            status = SyncStatus.PENDING,
            latitude = latitude, longitude = longitude
        )
    )
    override suspend fun pending(): List<TransactionEntity> = dao.getPending()
    override suspend fun markSynced(ids: List<Long>) {
        if (ids.isNotEmpty()) dao.bulkUpdateStatus(ids, SyncStatus.SYNCED)
    }
    override suspend fun getInRange(start: Long, end: Long): List<TransactionEntity> =
        dao.getInRange(start, end)
}
