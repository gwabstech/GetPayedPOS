package com.gwabs.getpayedpos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gwabs.getpayedpos.data.local.dao.AccountDao
import com.gwabs.getpayedpos.data.local.dao.IncomingTransferDao
import com.gwabs.getpayedpos.data.local.dao.TransactionDao

@Database(
    entities = [
        TransactionEntity::class,
        AccountEntity::class,
        IncomingTransferEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun incomingTransferDao(): IncomingTransferDao
}

