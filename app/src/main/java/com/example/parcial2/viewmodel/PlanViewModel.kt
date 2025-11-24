package com.example.parcial2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial2.model.Plan
import com.example.parcial2.repository.SavingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.parcial2.core.UiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class PlanViewModel(private val repo: SavingRepository) : ViewModel() {
    private val _plans = MutableStateFlow<UiState<List<Plan>>>(UiState.Loading)
    val plans: StateFlow<UiState<List<Plan>>> = _plans

    private val _planDetail = MutableStateFlow<UiState<Plan>>(UiState.Loading)
    val planDetail: StateFlow<UiState<Plan>> = _planDetail

    fun fetchPlans() {
        viewModelScope.launch {
            _plans.value = UiState.Loading
            try {
                val res = repo.getPlans()
                if (res.isSuccessful)
                    _plans.value = UiState.Success(res.body() ?: emptyList())
                else
                    _plans.value = UiState.Error("Error: ${res.code()}")
            } catch (e: Exception) {
                _plans.value = UiState.Error(e.message ?: "Error inesperado")
            }
        }
    }

    fun fetchPlanById(id: String) {
        viewModelScope.launch {
            _planDetail.value = UiState.Loading
            try {
                val res = repo.getPlanById(id)
                if (res.isSuccessful)
                    _planDetail.value = UiState.Success(res.body()!!)
                else
                    _planDetail.value = UiState.Error("Error: ${res.code()}")
            } catch (e: Exception) {
                _planDetail.value = UiState.Error(e.message ?: "Error inesperado")
            }
        }
    }
}
