package com.gwabs.getpayedpos.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gwabs.getpayedpos.data.local.AccountEntity
import com.gwabs.getpayedpos.data.repository.AccountRepository
import com.gwabs.getpayedpos.domain.usecase.SimulateIncomingPaymentUseCase

import com.gwabs.getpayedpos.ui.common.UiEvent
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch



class DashboardViewModel(
    private val accountRepo: AccountRepository,
    private val simulateIncoming: SimulateIncomingPaymentUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            accountRepo.account().collect { acct: AccountEntity? ->
                if (acct != null) {
                    _state.update { it.copy(accountNumber = acct.accountNumber, accountName = acct.accountName, bankName = acct.bankName, balance = acct.balance) }
                }
            }
        }
    }

    fun onAccountNumberClicked() {
        viewModelScope.launch {
            val event = simulateIncoming() // generates random amount + masked account, increments balance
            _events.tryEmit(UiEvent.PaymentReceived(event.amount, event.fromAccountNumber, fromAccountName = event.fromAccName, fromBankName = event.fromBankName))
        }
    }
}

data class DashboardState(
    val accountNumber: String = "0000000000",
    val accountName: String = "Abubakar Abdullahi",
    val bankName: String = "Getpayed",
    val balance: Double = 0.0
)