package com.gwabs.getpayedpos.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gwabs.getpayedpos.domain.usecase.SyncPendingTransactionsUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    private val syncUseCase: SyncPendingTransactionsUseCase by inject()

    override suspend fun doWork(): Result {
        val res = syncUseCase()
        return if (res.isSuccess) Result.success() else Result.retry()
    }
}