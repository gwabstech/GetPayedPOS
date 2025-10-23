package com.gwabs.getpayedpos.domain.usecase

import com.gwabs.getpayedpos.data.local.PaymentMethod
import com.gwabs.getpayedpos.data.repository.AccountRepository
import com.gwabs.getpayedpos.data.repository.TransactionRepository

class AddTransactionUseCase(
    private val txRepo: TransactionRepository,
    private val accountRepo: AccountRepository
) {
    suspend operator fun invoke(
        amount: Double,
        method: PaymentMethod,
        lat: Double?, lon: Double?,
        ts: Long = System.currentTimeMillis()
    ) {
        txRepo.addTransaction(amount, method, lat, lon, ts)
        accountRepo.incrementBalance(amount)
    }
}