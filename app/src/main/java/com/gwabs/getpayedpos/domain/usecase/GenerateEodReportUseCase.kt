package com.gwabs.getpayedpos.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.gwabs.getpayedpos.data.local.PaymentMethod
import com.gwabs.getpayedpos.data.local.SyncStatus
import com.gwabs.getpayedpos.data.repository.AccountRepository
import com.gwabs.getpayedpos.data.repository.TransactionRepository
import com.gwabs.getpayedpos.data.repository.TransferRepository
import com.gwabs.getpayedpos.domain.model.EodReport
import com.gwabs.getpayedpos.domain.model.EodTotals

class GenerateEodReportUseCase(
    private val txRepo: TransactionRepository,
    private val transferRepo: TransferRepository,
    private val accountRepo: AccountRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    private fun dayBounds(epochMillis: Long): Pair<Long, Long> {
        val zdt = java.time.Instant.ofEpochMilli(epochMillis)
            .atZone(java.time.ZoneId.systemDefault())
        val start = zdt.toLocalDate().atStartOfDay(zdt.zone).toInstant().toEpochMilli()
        val end = zdt.toLocalDate().plusDays(1).atStartOfDay(zdt.zone).minusNanos(1)
            .toInstant().toEpochMilli()
        return start to end
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(forEpochMillis: Long = System.currentTimeMillis()): EodReport {
        val (start, end) = dayBounds(forEpochMillis)

        val txs = txRepo.getInRange(start, end)
        val transfers = transferRepo.getInRange(start, end)
        val account = accountRepo.getSnapshot()

        val cash = txs.filter { it.paymentMethod == PaymentMethod.CASH }
        val card = txs.filter { it.paymentMethod == PaymentMethod.CARD }
        val transfer = txs.filter { it.paymentMethod == PaymentMethod.TRANSFER }


        val pending = txs.filter { it.status == SyncStatus.PENDING }
        val synced = txs.filter { it.status == SyncStatus.SYNCED }

        val cashSum = cash.sumOf { it.amount }
        val cardSum = card.sumOf { it.amount }

        val pendingSum = pending.sumOf { it.amount }
        val syncedSum = synced.sumOf { it.amount }

        val incomingSum = transfers.sumOf { it.amount }

        // Naive starting balance inference: endingBalance - (today’s tx + today’s incoming)
        // (We keep a running balance and back out today's activity.)
        val todaysActivity = cashSum + cardSum + incomingSum
        val ending = account.balance
        val starting = ending - todaysActivity

        return EodReport(
            dayStartMillis = start,
            dayEndMillis = end,
            cash = EodTotals(cash.size, cashSum),
            card = EodTotals(card.size, cardSum),
            pendingCount = pending.size,
            pendingSum = pendingSum,
            syncedCount = synced.size,
            syncedSum = syncedSum,
            incomingTransfersSum = incomingSum,
            startingBalance = starting,
            endingBalance = ending
        )
    }
}
