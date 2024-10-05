package com.qosquo.wallet.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.qosquo.wallet.Dependencies
import com.qosquo.wallet.ui.screens.categories.CategoriesList
import kotlin.reflect.KClass

@SuppressLint("RestrictedApi")
fun NavGraphBuilder.categoriesGraph(
    navController: NavController
) {
    navigation<Routes.Categories>(
        startDestination = Screens.Categories.List,
    ) {
        composable<Screens.Categories.List> {
            CategoriesList(
                onEvent = Dependencies.categoriesViewModel::onEvent,
                onActionButtonClicked = {

                }
            )
        }
    }
}