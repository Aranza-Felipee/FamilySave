package com.example.parcial2.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.parcial2.core.UiState
import com.example.parcial2.repository.SavingRepository
import com.example.parcial2.viewmodel.CreatePlanViewModel
import com.example.parcial2.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlanScreen(navController: NavController) {

    val repo = SavingRepository()
    val factory = ViewModelFactory(repo)
    val vm: CreatePlanViewModel = viewModel(factory = factory)

    var nombre by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }
    var meta by remember { mutableStateOf("") }
    var meses by remember { mutableStateOf("") }

    val state by vm.createState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Reacciona a cambios de estado
    LaunchedEffect(state) {
        when (state) {
            is UiState.Success -> {
                navController.popBackStack()
                vm.resetState()
            }
            is UiState.Error -> {
                snackbarHostState.showSnackbar((state as UiState.Error).message)
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
                "Crear plan",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del plan") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = motivo,
                onValueChange = { motivo = it },
                label = { Text("Motivo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = meta,
                onValueChange = { meta = it },
                label = { Text("Meta") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = meses,
                onValueChange = { meses = it },
                label = { Text("Meses") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val amountDouble = meta.toDoubleOrNull()
                    val monthsInt = meses.toIntOrNull()

                    if (nombre.isBlank() || motivo.isBlank() || meta.isBlank() || meses.isBlank()) {
                        vm.setError("Campos vacíos")
                        return@Button
                    }
                    if (amountDouble == null || monthsInt == null) {
                        vm.setError("Meta o meses no válidos")
                        return@Button
                    }

                    vm.createPlan(
                        name = nombre,
                        motive = motivo,
                        amount = amountDouble,
                        months = monthsInt
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state !is UiState.Loading
            ) {
                Text("Crear")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state is UiState.Loading) {
                CircularProgressIndicator()
            }
        }
    }
}
