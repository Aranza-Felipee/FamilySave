package com.example.parcial2.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.parcial2.core.UiState
import com.example.parcial2.model.Member
import com.example.parcial2.model.Payment
import com.example.parcial2.model.Plan
import com.example.parcial2.repository.SavingRepository
import com.example.parcial2.viewmodel.MemberViewModel
import com.example.parcial2.viewmodel.PaymentViewModel
import com.example.parcial2.viewmodel.PlanViewModel
import com.example.parcial2.viewmodel.ViewModelFactory

@Composable
fun SavingPlanDetailScreen(
    navController: NavController,
    planId: String
) {
    val factory = ViewModelFactory(SavingRepository())
    val planViewModel: PlanViewModel = viewModel(factory = factory)
    val paymentViewModel: PaymentViewModel = viewModel(factory = factory)
    val memberViewModel: MemberViewModel = viewModel(factory = factory)

    val planState by planViewModel.planDetail.collectAsState()
    val membersState by memberViewModel.members.collectAsState()
    val paymentsState by paymentViewModel.payments.collectAsState()
    val addMemberState by memberViewModel.addMemberState.collectAsState()

    var showAddMemberDialog by remember { mutableStateOf(false) }
    var memberName by remember { mutableStateOf("") }

    LaunchedEffect(planId) {
        Log.d("Screen", "Calling fetchMembersByPlan with $planId")
        planViewModel.fetchPlanById(planId)
        memberViewModel.fetchMembersByPlan(planId) //
        paymentViewModel.fetchPaymentsByPlan(planId)
    }


    LaunchedEffect(addMemberState) {
        if (addMemberState is UiState.Success) {

            memberViewModel.fetchMembersByPlan(planId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (planState) {
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> {
                val plan = (planState as UiState.Success<Plan>).data
                Text(plan.name ?: "Sin nombre", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("${plan.durationInMonths ?: 0} meses")
                Text("$${plan.goalAmount ?: 0.0}")

                Spacer(Modifier.height(16.dp))

                if (!plan.id.isNullOrBlank()) {
                    Button(
                        onClick = { navController.navigate("register_payment/${plan.id}") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Registrar pago")
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { showAddMemberDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Añadir miembro")
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text("Miembros", fontSize = 20.sp)
                Spacer(Modifier.height(8.dp))

                when (membersState) {
                    is UiState.Loading -> CircularProgressIndicator()
                    is UiState.Success -> {
                        val members = (membersState as UiState.Success<List<Member>>).data
                        LazyColumn {
                            items(members) { member ->
                                MemberItem(member)
                            }
                        }
                    }
                    is UiState.Error -> Text(
                        (membersState as UiState.Error).message,
                        color = Color.Red
                    )
                    else -> {}
                }
            }
            is UiState.Error -> Text(
                "Error: ${(planState as UiState.Error).message}",
                color = Color.Red
            )
            else -> {}
        }

        Spacer(Modifier.height(16.dp))

        Text("Pagos", fontSize = 20.sp)
        when (paymentsState) {
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> {
                val payments = (paymentsState as UiState.Success<List<Payment>>).data
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(payments) { payment ->
                        PaymentItem(payment)
                    }
                }
            }
            is UiState.Error -> Text(
                "Error: ${(paymentsState as UiState.Error).message}", color = Color.Red
            )
            else -> {}
        }
    }

    if (showAddMemberDialog) {
        Dialog(onDismissRequest = { showAddMemberDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Agregar miembro",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = memberName,
                        onValueChange = { memberName = it },
                        label = { Text("Nombre del miembro") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { showAddMemberDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Cancelar")
                        }
                        Button(
                            onClick = {
                                Log.d("SaveMember", "Guardando miembro. Nombre: '$memberName', Plan ID: $planId")
                                if (memberName.isNotBlank()) {
                                    memberViewModel.addMember(planId, memberName)
                                } else {
                                    Log.d("SaveMember", "El nombre del miembro está vacío. No se guardará.")
                                }
                                showAddMemberDialog = false
                                memberName = ""
                            }
                        ) {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MemberItem(member: Member) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = member.name ?: "Sin nombre",
            fontSize = 18.sp
        )
    }
}

@Composable
fun PaymentItem(payment: Payment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(payment.member?.name ?: "Miembro desconocido", Modifier.weight(1f))
        Text("$${payment.amount}", fontWeight = FontWeight.Bold)
    }
}
