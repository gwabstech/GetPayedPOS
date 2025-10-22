package com.gwabs.getpayedpos.net.di

import androidx.work.WorkManager
import com.gwabs.getpayedpos.net.ConnectivityMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val connectivityModule = module {
    single { WorkManager.getInstance(androidContext()) }
    single { ConnectivityMonitor(androidContext(), get()) }
}