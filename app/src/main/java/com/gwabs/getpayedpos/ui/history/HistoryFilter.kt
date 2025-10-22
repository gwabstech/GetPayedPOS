package com.gwabs.getpayedpos.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.gwabs.getpayedpos.data.local.PaymentMethod
import com.gwabs.getpayedpos.data.local.SyncStatus
import com.gwabs.getpayedpos.data.local.TransactionEntity
import com.gwabs.getpayedpos.data.repository.TransactionRepository
import kotlinx.coroutines.flow.*

enum class HistoryFilter { ALL, CASH, CARD, TRANSFER, PENDING, SYNCED }

data class HistoryState(
    val filter: HistoryFilter = HistoryFilter.ALL,
    val items: List<TransactionEntity> = emptyList()
)

class HistoryViewModel(
    private val repo: TransactionRepository
) : ViewModel() {

    private val filter = MutableStateFlow(HistoryFilter.ALL)
    private val all = repo.transactions()

    val state: StateFlow<HistoryState> =
        combine(filter, all) { f, list ->
            HistoryState(
                filter = f,
                items = when (f) {
                    HistoryFilter.ALL -> list
                    HistoryFilter.CASH -> list.filter { it.paymentMethod == PaymentMethod.CASH }
                    HistoryFilter.CARD -> list.filter { it.paymentMethod == PaymentMethod.CARD }
                    HistoryFilter.TRANSFER -> list.filter { it.paymentMethod == PaymentMethod.TRANSFER }
                    HistoryFilter.PENDING -> list.filter { it.status == SyncStatus.PENDING }
                    HistoryFilter.SYNCED -> list.filter { it.status == SyncStatus.SYNCED }
                }
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HistoryState())

    fun setFilter(new: HistoryFilter) { filter.value = new }
}
