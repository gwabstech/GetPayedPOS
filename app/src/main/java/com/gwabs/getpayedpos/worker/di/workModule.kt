package com.gwabs.getpayedpos.worker.di
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.dsl.module

val workModule = module {
    single { KoinWorkerFactory() }
}