package com.gwabs.getpayedpos.ui.eod

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gwabs.getpayedpos.domain.model.EodReport
import com.gwabs.getpayedpos.domain.usecase.GenerateEodReportUseCase

import com.gwabs.getpayedpos.ui.common.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EodReportViewModel(
    private val generate: GenerateEodReportUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ResultState<EodReport>>(ResultState.Idle)
    val state: StateFlow<ResultState<EodReport>> = _state.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadToday() {
        viewModelScope.launch {
            _state.value = ResultState.Loading
            val res = runCatching { generate() }
            _state.value = res.fold(
                onSuccess = { ResultState.Success(it) },
                onFailure = { ResultState.Failure(it.message ?: "Failed to build EOD") }
            )
        }
    }
}
