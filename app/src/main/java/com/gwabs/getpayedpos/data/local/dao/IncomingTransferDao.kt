package com.gwabs.getpayedpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gwabs.getpayedpos.data.local.IncomingTransferEntity

// data/local/IncomingTransferDao.kt
@Dao
interface IncomingTransferDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: IncomingTransferEntity): Long

    @Query("""
        SELECT * FROM incoming_transfers
        WHERE timestampMillis BETWEEN :start AND :end
        ORDER BY timestampMillis ASC
    """)
    suspend fun getInRange(start: Long, end: Long): List<IncomingTransferEntity>
}
