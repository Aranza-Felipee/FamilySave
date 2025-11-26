package com.example.parcial2.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.parcial2.core.UiState
import com.example.parcial2.model.Plan
import com.example.parcial2.repository.SavingRepository
import com.example.parcial2.viewmodel.PlanViewModel
import com.example.parcial2.viewmodel.ViewModelFactory

@Composable
fun FamilySavingPlanScreen(navController: NavController) {

    val repo = SavingRepository()
    val factory = ViewModelFactory(repo)
    val owner = LocalViewModelStoreOwner.current

    val vm: PlanViewModel = viewModel(
        viewModelStoreOwner = owner!!,
        factory = factory
    )

    val plansState by vm.plans.collectAsState()

    LaunchedEffect(Unit) {
        vm.fetchPlans()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Plan Familiar de Ahorro",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("create_plan") }) {
            Text(text = "Crear plan")
        }
        Spacer(modifier = Modifier.height(12.dp))

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Planes de ahorro",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(16.dp))

        when (val state = plansState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Success -> {
                val plans = state.data
                if (plans.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay planes de ahorro todavía. ¡Crea uno!")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(plans) { plan ->
                            SavingPlanItem(plan = plan, navController = navController)
                        }
                    }
                }
            }
            is UiState.Error -> {
                Text(text = "Error: ${state.message}", color = Color.Red)
            }
            else -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay información disponible.")
                }
            }
        }
    }
}


@Composable
fun SavingPlanItem(plan: Plan, navController: NavController) {
    val modifier = if (!plan.id.isNullOrBlank()) {
        Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("saving_plan_detail/${plan.id}") }
    } else {
        Modifier.fillMaxWidth()
    }

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = plan.name ?: "Sin nombre",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = plan.category ?: "Sin categoría",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$${plan.goalAmount}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${plan.durationInMonths} meses",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
