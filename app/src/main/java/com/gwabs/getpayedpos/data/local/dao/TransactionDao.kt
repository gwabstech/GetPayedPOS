package com.gwabs.getpayedpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gwabs.getpayedpos.data.local.SyncStatus
import com.gwabs.getpayedpos.data.local.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.Companion.ABORT)
    suspend fun insert(entity: TransactionEntity): Long

    @Query("SELECT * FROM transactions ORDER BY timestampMillis DESC")
    fun observeAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE status = 'PENDING' ORDER BY timestampMillis ASC")
    suspend fun getPending(): List<TransactionEntity>

    @Query("UPDATE transactions SET status = :newStatus WHERE id IN (:ids)")
    suspend fun bulkUpdateStatus(ids: List<Long>, newStatus: SyncStatus)

    @Query("DELETE FROM transactions")
    suspend fun clearAll()

    @Query("""
        SELECT * FROM transactions 
        WHERE timestampMillis BETWEEN :start AND :end
        ORDER BY timestampMillis ASC
    """)
    suspend fun getInRange(start: Long, end: Long): List<TransactionEntity>
}