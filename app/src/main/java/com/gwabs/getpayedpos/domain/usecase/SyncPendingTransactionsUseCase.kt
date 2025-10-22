package com.gwabs.getpayedpos.domain.usecase

import com.gwabs.getpayedpos.data.repository.TransactionRepository
import com.gwabs.getpayedpos.domain.sync.toDto
import com.gwabs.getpayedpos.network.PosApi
import com.gwabs.getpayedpos.network.dto.SyncRequest

class SyncPendingTransactionsUseCase(
    private val txRepo: TransactionRepository,
    private val api: PosApi
) {
    suspend operator fun invoke(): Result<Int> = runCatching {
        val pending = txRepo.pending()
        if (pending.isEmpty()) return Result.success(0)
        val body = SyncRequest(pending.map { it.toDto() })
        val resp = api.sync(body)
        txRepo.markSynced(resp.syncedIds)
        resp.syncedIds.size
    }
}