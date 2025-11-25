package com.example.parcial2.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial2.model.Plan
import com.example.parcial2.repository.SavingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.parcial2.core.UiState
import com.example.parcial2.model.Member
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class PlanViewModel(private val repo: SavingRepository) : ViewModel() {
    private val _plans = MutableStateFlow<UiState<List<Plan>>>(UiState.Loading)
    val plans: StateFlow<UiState<List<Plan>>> = _plans

    private val _planDetail = MutableStateFlow<UiState<Plan>>(UiState.Loading)
    val planDetail: StateFlow<UiState<Plan>> = _planDetail


    private val _members = MutableStateFlow<UiState<List<Member>>>(UiState.Idle)
    val members: StateFlow<UiState<List<Member>>> get() = _members

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
                if (res.isSuccessful && res.body() != null) {
                    _planDetail.value = UiState.Success(res.body()!!)
                } else {
                    _planDetail.value = UiState.Error("Error: ${res.code()} o plan no encontrado")
                }
            } catch (e: Exception) {
                _planDetail.value = UiState.Error(e.message ?: "Error inesperado")
            }
        }
    }

    fun fetchMembersByPlan(planId: String) {
        viewModelScope.launch {
            Log.d("PlanVM", "Fetching members for planId: $planId")
            _members.value = UiState.Loading
            try {
                val res = repo.getMembersByPlan(planId) // Usando la función corregida
                if (res.isSuccessful) {
                    val body = res.body()
                    if (body.isNullOrEmpty()) {
                        _members.value = UiState.Error("No hay miembros en este plan")
                    } else {
                        _members.value = UiState.Success(body)
                    }
                } else {
                    _members.value = UiState.Error("Error: ${res.code()}")
                }
            } catch (e: Exception) {
                _members.value = UiState.Error(e.message ?: "Error inesperado")
            }
        }
    }
}
