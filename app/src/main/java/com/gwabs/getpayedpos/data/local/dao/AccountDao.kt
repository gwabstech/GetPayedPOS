package com.gwabs.getpayedpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gwabs.getpayedpos.data.local.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM account WHERE id = 1")
    fun observeAccount(): Flow<AccountEntity?>

    @Query("SELECT * FROM account WHERE id = 1")
    suspend fun getAccount(): AccountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: AccountEntity)

    @Query("UPDATE account SET balance = balance + :delta WHERE id = 1")
    suspend fun incrementBalance(delta: Double)
}
