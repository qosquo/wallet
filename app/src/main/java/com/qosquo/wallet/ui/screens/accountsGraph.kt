package com.qosquo.wallet.ui.screens

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.qosquo.wallet.Dependencies
import com.qosquo.wallet.Dependencies.accountsViewModel
import com.qosquo.wallet.ui.screens.accounts.AccountsForm
import com.qosquo.wallet.ui.screens.accounts.AccountsList
import com.qosquo.wallet.viewmodel.AccountsViewModel

fun NavGraphBuilder.accountsGraph(
    route: String,
    navController: NavController
) {
    navigation(
        startDestination = Screen.Accounts.List.route,
        route = route
    ) {
        // accounts/list
        composable(Screen.Accounts.List.route) {
            AccountsList(
                onEvent = accountsViewModel::onEvent,
                onActionButtonClicked = { isEditing ->
                    if (isEditing) {
                        navController.navigate(Screen.Accounts.Edit.route)
                    } else {
                        navController.navigate(Screen.Accounts.Create.route)
                    }
                }
            )
        }
        composable(Screen.Accounts.Create.route) {
            AccountsForm(
                onEvent = accountsViewModel::onEvent,
                onFinishCreating = {
                    navController.navigateUp()
                }
            )
        }

        composable(Screen.Accounts.Edit.route) {
            AccountsForm(
                onEvent = accountsViewModel::onEvent,
                onFinishCreating = {
                    navController.navigateUp()
                }
            )
        }
    }
}