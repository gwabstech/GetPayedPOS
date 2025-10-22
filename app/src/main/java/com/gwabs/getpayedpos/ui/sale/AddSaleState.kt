package com.gwabs.getpayedpos.ui.sale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gwabs.getpayedpos.data.local.PaymentMethod
import com.gwabs.getpayedpos.data.repository.AccountRepository
import com.gwabs.getpayedpos.data.repository.TransactionRepository
import com.gwabs.getpayedpos.domain.location.LocationProvider

import com.gwabs.getpayedpos.ui.common.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AddSaleState(
    val submitting: Boolean = false,
    val lastSubmit: ResultState<Unit> = ResultState.Idle
)

class AddSaleViewModel(
    private val txRepo: TransactionRepository,
    private val accountRepo: AccountRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _state = MutableStateFlow(AddSaleState())
    val state: StateFlow<AddSaleState> = _state.asStateFlow()

    fun addCash(amount: Double) = submit(amount, PaymentMethod.CASH)
    fun addCard(amount: Double) = submit(amount, PaymentMethod.CARD)
    fun addTransfer(amount: Double) = submit(amount, PaymentMethod.TRANSFER)

    private fun submit(amount: Double, method: PaymentMethod) {
        viewModelScope.launch {
            _state.value = _state.value.copy(submitting = true, lastSubmit = ResultState.Loading)
            val loc = locationProvider.getLocationOrNull()
            val lat = loc.latitude
            val lon = loc.longitude

            runCatching {
                txRepo.addTransaction(amount, method, lat, lon)
                accountRepo.incrementBalance(amount)
            }.onSuccess {
                _state.value = _state.value.copy(submitting = false, lastSubmit = ResultState.Success(Unit))
            }.onFailure { e ->
                _state.value = _state.value.copy(submitting = false, lastSubmit = ResultState.Failure(e.message ?: "Failed"))
            }
        }
    }
}
