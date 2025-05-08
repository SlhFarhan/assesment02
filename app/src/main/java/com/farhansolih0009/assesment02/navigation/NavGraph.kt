package com.farhansolih0009.assesment02.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.farhansolih0009.assesment02.ui.screen.DetailScreen
import com.farhansolih0009.assesment02.ui.screen.KEY_ID_TRANSAKSI
import com.farhansolih0009.assesment02.ui.screen.MainScreen
import com.farhansolih0009.assesment02.ui.screen.RecycleBinScreen


@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(navController)
        }
        composable(route = Screen.FormBaru.route) {
            DetailScreen(navController)
        }
        composable(route = Screen.RecycleBin.route) {
            RecycleBinScreen(navController)
        }
        composable(
            route = Screen.FormUbah.route,
            arguments = listOf(
                navArgument(KEY_ID_TRANSAKSI) { type = NavType.LongType }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_TRANSAKSI)
            DetailScreen(navController, id)

        }

    }
}