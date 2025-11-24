package com.example.parcial2.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

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
        composable(
            route = "saving_plan_detail/{planId}",
            arguments = listOf(navArgument("planId") { type = NavType.StringType })
        ) { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId")
            if (planId != null) {
                SavingPlanDetailScreen(navController = navController, planId = planId)
            }
        }
        composable(
            route = "register_payment/{planId}",
            arguments = listOf(navArgument("planId") { type = NavType.StringType })
        ) { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId")
            if (planId != null) {
                RegisterPaymentScreen(navController = navController, planId = planId)
            }
        }
    }
}
