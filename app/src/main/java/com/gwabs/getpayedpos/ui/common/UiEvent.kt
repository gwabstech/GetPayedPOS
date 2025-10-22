package com.gwabs.getpayedpos.ui.common

sealed class UiEvent {
    data class PaymentReceived(
        val amount: Double,
        val fromAccount: String,
        val fromAccountName: String,
        val fromBankName: String
    ) : UiEvent()
    data class Error(val message: String) : UiEvent()
}
