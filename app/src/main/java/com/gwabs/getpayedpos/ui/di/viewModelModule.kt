package com.gwabs.getpayedpos.ui.di

import com.gwabs.getpayedpos.ui.dashboard.DashboardViewModel
import com.gwabs.getpayedpos.ui.eod.EodReportViewModel
import com.gwabs.getpayedpos.ui.sale.AddSaleViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { DashboardViewModel(get(), get()) }
    viewModel { AddSaleViewModel(get(), get(), get()) }
    viewModel { EodReportViewModel(get()) }
    viewModel { com.gwabs.getpayedpos.ui.history.HistoryViewModel(get()) }

}
