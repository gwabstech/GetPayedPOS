package com.gwabs.getpayedpos.domain.di

import com.gwabs.getpayedpos.domain.usecase.AddTransactionUseCase
import com.gwabs.getpayedpos.domain.usecase.GenerateEodReportUseCase
import com.gwabs.getpayedpos.domain.usecase.SimulateIncomingPaymentUseCase
import com.gwabs.getpayedpos.domain.usecase.SyncPendingTransactionsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { AddTransactionUseCase(get(), get()) }
    factory { SimulateIncomingPaymentUseCase(get(), get()) }
    factory { GenerateEodReportUseCase(get(), get(), get()) }
    factory { SyncPendingTransactionsUseCase(get(), get()) }


}
