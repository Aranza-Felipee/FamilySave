package com.example.parcial2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial2.model.Payment
import com.example.parcial2.model.PaymentRequest
import com.example.parcial2.repository.SavingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.parcial2.core.UiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(private val repo: SavingRepository) : ViewModel() {

    private val _payments = MutableStateFlow<UiState<List<Payment>>>(UiState.Loading)
    val payments: StateFlow<UiState<List<Payment>>> = _payments

    fun fetchPaymentsByPlan(planId: String) {
        viewModelScope.launch {
            _payments.value = UiState.Loading
            try {
                val res = repo.getPaymentsByPlan(planId)
                if (res.isSuccessful)
                    _payments.value = UiState.Success(res.body() ?: emptyList())
                else
                    _payments.value = UiState.Error("Error: ${res.code()}")
            } catch (e: Exception) {
                _payments.value = UiState.Error(e.message ?: "Error inesperado")
            }
        }
    }

    private val _paymentResult = MutableStateFlow<UiState<Payment>>(UiState.Loading)
    val paymentResult: StateFlow<UiState<Payment>> = _paymentResult

    fun registerPayment(req: PaymentRequest) {
        viewModelScope.launch {
            _paymentResult.value = UiState.Loading
            try {
                val res = repo.createPayment(req)
                if (res.isSuccessful)
                    _paymentResult.value = UiState.Success(res.body()!!)
                else
                    _paymentResult.value = UiState.Error("Error: ${res.code()}")
            } catch (e: Exception) {
                _paymentResult.value = UiState.Error(e.message ?: "Error inesperado")
            }
        }
    }
}
