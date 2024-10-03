package com.qosquo.wallet.ui.screens

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.qosquo.wallet.Dependencies
import com.qosquo.wallet.ui.screens.categories.CategoriesList

fun NavGraphBuilder.categoriesGraph(
    route: String,
    navController: NavController
) {
    navigation(
        startDestination = Screen.Categories.List.route,
        route = route
    ) {
        composable(Screen.Categories.List.route) {
            CategoriesList(
                onEvent = Dependencies.categoriesViewModel::onEvent,
                onActionButtonClicked = {

                }
            )
        }
    }
}