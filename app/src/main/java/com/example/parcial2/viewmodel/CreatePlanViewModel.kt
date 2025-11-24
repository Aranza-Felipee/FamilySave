package com.example.parcial2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial2.core.UiState
import com.example.parcial2.model.Plan
import com.example.parcial2.repository.SavingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreatePlanViewModel(private val repo: SavingRepository) : ViewModel() {


    private val _createState = MutableStateFlow<UiState<Plan>>(UiState.Idle)
    val createState: StateFlow<UiState<Plan>> = _createState

    fun createPlan(name: String, motive: String, amount: Double, months: Int) {
        viewModelScope.launch {
            try {
                val plan = Plan(
                    id = "",
                    name = name,
                    category = motive,
                    goalAmount = amount,
                    durationInMonths = months,
                    createdAt = "",
                    members = emptyList()
                )

                val res = repo.createPlan(plan)

                if (res.isSuccessful) {

                    val body = res.body()

                    if (body != null) {
                        _createState.value = UiState.Success(body)
                    } else {
                        _createState.value = UiState.Error("La API respondió vacío")
                    }

                } else {
                    _createState.value = UiState.Error("Error: ${res.code()}")
                }

            } catch (e: Exception) {
                _createState.value = UiState.Error(e.message ?: "Error inesperado")
            }
        }
    }

    fun setError(message: String) {
        _createState.value = UiState.Error(message)
    }



    fun resetState() {
        _createState.value = UiState.Loading
    }
}