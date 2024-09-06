package com.qosquo.wallet.ui.screens

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun NavGraphBuilder.categoriesGraph(
    route: String,
    navController: NavController
) {
    val screens = listOf(
        Screen.Categories.List,
    )

    navigation(
        startDestination = screens[0].route,
        route = route
    ) {
        composable(Screen.Categories.List.route) {
            Text("hello")
        }
    }
}