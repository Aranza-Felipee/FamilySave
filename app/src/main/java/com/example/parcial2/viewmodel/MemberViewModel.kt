package com.example.parcial2.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.parcial2.core.UiState
import com.example.parcial2.model.AddMemberRequest
import com.example.parcial2.model.Member
import com.example.parcial2.repository.SavingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemberViewModel(private val repository: SavingRepository) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _members = MutableStateFlow<UiState<List<Member>>>(UiState.Loading)
    val members = _members.asStateFlow()

    private val _addMemberState = MutableStateFlow<UiState<Member?>>(UiState.Empty)
    val addMemberState = _addMemberState

    fun addMember(planId: String, name: String) {
        uiScope.launch {
            _addMemberState.value = UiState.Loading
            Log.d("MemberViewModel", "Iniciando addMember para plan $planId con nombre $name")

            try {
                val newMember = AddMemberRequest(
                    name = name,
                    planId = planId,
                    contributionPerMonth = 0
                )

                val response = withContext(Dispatchers.IO) {
                    repository.createMember(newMember)
                }

                if (response.isSuccessful) {
                    Log.d("MemberViewModel", "Miembro añadido con éxito")
                    _addMemberState.value = UiState.Success(response.body())
                } else {
                    Log.e("MemberViewModel", "Error al añadir miembro: ${response.code()} - ${response.errorBody()?.string()}")
                    _addMemberState.value = UiState.Error("Error ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MemberViewModel", "Excepción al añadir miembro", e)
                _addMemberState.value = UiState.Error(e.message ?: "Error desconocido")
            }
        }
    }


    fun fetchMembersByPlan(planId: String) {
        uiScope.launch {
            _members.value = UiState.Loading
            try {
                val response = withContext(Dispatchers.IO) {
                    repository.getMembersByPlan(planId)
                }
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
