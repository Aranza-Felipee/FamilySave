package com.example.parcial2.view

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

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.parcial2.core.UiState
import com.example.parcial2.model.Member
import com.example.parcial2.model.Payment
import com.example.parcial2.model.Plan
import com.example.parcial2.repository.SavingRepository
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

    val planState by planViewModel.planDetail.collectAsState()
    val paymentsState by paymentViewModel.payments.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Cargar datos al iniciar
    LaunchedEffect(planId) {
        planViewModel.fetchPlanById(planId)
        paymentViewModel.fetchPaymentsByPlan(planId)
    }

    // Mostrar errores en Snackbar
    LaunchedEffect(planState, paymentsState) {
        (planState as? UiState.Error)?.let { snackbarHostState.showSnackbar(it.message) }
        (paymentsState as? UiState.Error)?.let { snackbarHostState.showSnackbar(it.message) }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {

            // ----- PLAN -----
            when (val planData = planState) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Success -> {
                    val plan = planData.data
                    Text(plan.name ?: "Sin nombre", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("${plan.durationInMonths ?: 0} meses")
                    Spacer(Modifier.height(8.dp))
                    Text("$${plan.goalAmount ?: 0.0}")

                    Spacer(Modifier.height(32.dp))
                    Text("Miembros", fontSize = 20.sp)
                    Spacer(Modifier.height(16.dp))

                    LazyColumn {
                        items(plan.members ?: emptyList()) { member ->
                            MemberItem(member)
                        }
                    }
                }
                else -> {} // Idle o Error ya manejado por Snackbar
            }

            Spacer(Modifier.height(16.dp))

            // ----- PAGOS -----
            Text("Pagos", fontSize = 20.sp)
            Spacer(Modifier.height(16.dp))

            when (val paymentsData = paymentsState) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Success -> {
                    val payments = paymentsData.data
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(payments) { payment ->
                            PaymentItem(payment)
                        }
                    }
                }
                else -> {} // Idle o Error ya manejado
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("register_payment/$planId") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar pago")
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
        Text(payment.member.name ?: "Miembro desconocido", Modifier.weight(1f))
        Text("$${payment.amount}", fontWeight = FontWeight.Bold)
    }
}