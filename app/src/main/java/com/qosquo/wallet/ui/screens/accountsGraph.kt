package com.qosquo.wallet.ui.screens

import android.annotation.SuppressLint
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.qosquo.wallet.Dependencies
import com.qosquo.wallet.Dependencies.accountsViewModel
import com.qosquo.wallet.ui.screens.accounts.AccountsForm
import com.qosquo.wallet.ui.screens.accounts.AccountsList
import com.qosquo.wallet.viewmodel.AccountsViewModel
import kotlin.reflect.KClass

@SuppressLint("RestrictedApi")
fun NavGraphBuilder.accountsGraph(
    navController: NavController
) {
    navigation<Routes.Accounts> (
        startDestination = Screens.Accounts.List,
    ) {
        // accounts/list
        composable<Screens.Accounts.List> {
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
        composable<Screens.Accounts.Form> {
            AccountsForm(
                onEvent = accountsViewModel::onEvent,
                onFinishCreating = {
                    navController.navigateUp()
                }
            )
        }

//        composable(Screen.Accounts.Edit.route) {
//            AccountsForm(
//                onEvent = accountsViewModel::onEvent,
//                onFinishCreating = {
//                    navController.navigateUp()
//                }
//            )
//        }
    }
}