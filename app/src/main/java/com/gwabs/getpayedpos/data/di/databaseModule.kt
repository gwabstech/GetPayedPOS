package com.gwabs.getpayedpos.data.di

import androidx.room.Room
import com.gwabs.getpayedpos.data.local.AppDatabase
import com.gwabs.getpayedpos.data.repository.AccountRepository
import com.gwabs.getpayedpos.data.repository.AccountRepositoryImpl
import com.gwabs.getpayedpos.data.repository.TransactionRepository
import com.gwabs.getpayedpos.data.repository.TransactionRepositoryImpl
import com.gwabs.getpayedpos.data.repository.TransferRepository
import com.gwabs.getpayedpos.data.repository.TransferRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

// di/DatabaseModule.kt  (add lines)
val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "pos.db")
            .fallbackToDestructiveMigration(false)
            .build()
    }
    single { get<AppDatabase>().transactionDao() }
    single { get<AppDatabase>().accountDao() }
    single { get<AppDatabase>().incomingTransferDao() }

    single<TransactionRepository> { TransactionRepositoryImpl(get()) }
    single<AccountRepository> { AccountRepositoryImpl(get()) }
    single<TransferRepository> { TransferRepositoryImpl(get()) }
}
