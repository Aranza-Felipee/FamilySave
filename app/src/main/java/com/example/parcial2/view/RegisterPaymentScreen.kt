package com.example.parcial2.view

import android.R.attr.onClick
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.parcial2.core.UiState
import com.example.parcial2.model.PaymentRequest
import com.example.parcial2.model.Plan
import com.example.parcial2.repository.SavingRepository
import com.example.parcial2.viewmodel.PaymentViewModel
import com.example.parcial2.viewmodel.PlanViewModel
import com.example.parcial2.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPaymentScreen(
    navController: NavController,
    planId: String,
    planViewModel: PlanViewModel = viewModel(factory = ViewModelFactory(SavingRepository())),
    paymentViewModel: PaymentViewModel = viewModel(factory = ViewModelFactory(SavingRepository()))
) {
    val planState by planViewModel.planDetail.collectAsState()
    val paymentResultState by paymentViewModel.paymentResult.collectAsState()

    var selectedMember by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(planId) {
        planViewModel.fetchPlanById(planId)
    }

    // Observa los resultados de registro de pago
    LaunchedEffect(paymentResultState) {
        when (paymentResultState) {
            is UiState.Success -> {
                navController.popBackStack()
                paymentViewModel.resetState()
            }
            is UiState.Error -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar((paymentResultState as UiState.Error).message)
                }
                paymentViewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Registrar pago",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))

            when (planState) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Success -> {
                    val plan = (planState as UiState.Success<Plan>).data

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = selectedMember,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Miembro") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextField(
                                value = selectedMember,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Miembro") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                (plan.members ?: emptyList()).forEach { member ->
                                    val memberName = member.name ?: "Sin nombre"
                                    DropdownMenuItem(
                                        text = { Text(memberName) },
                                        onClick = {
                                            selectedMember = memberName
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Text("Error: ${(planState as UiState.Error).message}", color = Color.Red)
                }

                else -> {}
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Monto") },
                leadingIcon = { Text("$") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull()
                    if (selectedMember.isBlank() || amountDouble == null) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Miembro o monto inválido")
                        }
                        return@Button
                    }
                    val paymentRequest = PaymentRequest(
                        planId = planId,
                        memberName = selectedMember,
                        amount = amountDouble
                    )
                    paymentViewModel.registerPayment(paymentRequest)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedMember.isNotEmpty() && amount.isNotEmpty()
            ) {
                Text("Registrar")
            }

            if (paymentResultState is UiState.Loading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }
        }
    }
}