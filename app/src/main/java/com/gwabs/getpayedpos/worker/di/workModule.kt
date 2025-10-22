package com.gwabs.getpayedpos.worker.di
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.dsl.module

val workModule = module {
    single { KoinWorkerFactory() } // WorkManager will use this to inject workers
}