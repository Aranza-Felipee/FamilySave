package com.example.parcial2.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "family_saving_plan", modifier = modifier) {
        composable("family_saving_plan") {
            FamilySavingPlanScreen(navController)
        }
        composable("create_plan") {
            CreatePlanScreen(navController)
        }
        composable("saving_plan_detail") {
            SavingPlanDetailScreen(navController)
        }
        composable("register_payment") {
            RegisterPaymentScreen(navController)
        }
    }
}
