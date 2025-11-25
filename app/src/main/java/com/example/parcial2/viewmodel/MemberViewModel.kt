package com.example.parcial2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial2.core.UiState
import com.example.parcial2.model.Member
import com.example.parcial2.repository.SavingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MemberViewModel(private val repository: SavingRepository) : ViewModel() {

    private val _members = MutableStateFlow<UiState<List<Member>>>(UiState.Loading)
    val members = _members.asStateFlow()

    fun fetchMembersByPlan(planId: String) {
        viewModelScope.launch {
            _members.value = UiState.Loading
            try {
                val response = repository.getMembersByPlan(planId)
                if (response.isSuccessful) {
                    val members = response.body()
                    if (members.isNullOrEmpty()) {
                        _members.value = UiState.Error("No se encontraron miembros para este plan.")
                    } else {
                        _members.value = UiState.Success(members)
                    }
                } else {
                    _members.value = UiState.Error("Error ${response.code()}")
                }
            } catch (e: Exception) {
                _members.value = UiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
