package com.qosquo.wallet.ui.screens

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.qosquo.wallet.Dependencies.accountsViewModel
import com.qosquo.wallet.WalletAppBar
import com.qosquo.wallet.WalletNavigationBar
import com.qosquo.wallet.ui.screens.accounts.AccountsForm
import com.qosquo.wallet.ui.screens.accounts.AccountsList
import com.qosquo.wallet.viewmodel.AccountsViewModel

@Composable
fun AccountsScreen(
    viewModel: AccountsViewModel,
    navController: NavHostController = rememberNavController()
) {
    val routes = listOf(
        Screens.Accounts.List,
        Screens.Accounts.Create,
        Screens.Accounts.Edit
    )
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
//    val currentScreen = Routes.Accounts.valueOf(
//        backStackEntry?.destination?.route ?: Screen.Accounts.List.route
//    )
    val currentScreen = routes.find {
        it.route == backStackEntry?.destination?.route
    } ?: Screens.Accounts.List

    Scaffold(
        topBar = {
            WalletAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {
                    if (viewModel.canExitForm()) {
                        navController.navigateUp()
                    }
                }
            )
        },
        bottomBar = {
            WalletNavigationBar(
                navController = navController,
            )
        }
    ) { innerPadding ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        NavHost(
            navController = navController,
            startDestination = Screens.Accounts.List.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.Accounts.List.route) {
                AccountsList(
                    state = state,
                    onEvent = accountsViewModel::onEvent,
                    onActionButtonClicked = { isEditing ->
                        if (isEditing) {
                            navController.navigate(Screens.Accounts.Edit.route)
                        } else {
                            navController.navigate(Screens.Accounts.Create.route)
                        }
                    }
                )
            }

            composable(Screens.Accounts.Create.route) {
                AccountsForm(
                    state = state,
                    onEvent = accountsViewModel::onEvent,
                    onFinishCreating = {
                        navController.navigateUp()
                    }
                )
            }

            composable(Screens.Accounts.Edit.route) {
                AccountsForm(
                    state = state,
                    onEvent = accountsViewModel::onEvent,
                    onFinishCreating = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}