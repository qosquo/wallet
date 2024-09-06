package com.qosquo.wallet

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.qosquo.wallet.Dependencies.accountsViewModel
import com.qosquo.wallet.ui.screens.Screen
import com.qosquo.wallet.ui.screens.accounts.AccountsForm
import com.qosquo.wallet.ui.screens.accounts.AccountsList
import com.qosquo.wallet.ui.screens.accountsGraph
import com.qosquo.wallet.ui.screens.categoriesGraph
import com.qosquo.wallet.viewmodel.AccountsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletAppBar(
    currentScreen: Screen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.resourceId)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "back button"
                    )
                }
            }
        }
    )
}

@Composable
fun WalletNavigationBar(
    items: List<Screen>,
    navController: NavController,
    currentDestination: NavDestination?
) {
//    val items = listOf(
//        Screens.Accounts.List,
//    )
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.navigationBarsPadding()
    ) {
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(
                    ImageVector.vectorResource(id = screen.iconId),
                    contentDescription = null
                ) },
                label = { Text(stringResource(screen.resourceId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun WalletApp(
    navController: NavHostController = rememberNavController()
) {
    val screens = listOf(
        Screen.Accounts.Root,
        Screen.Categories.Root
    )
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = screens.find {
        it.route == backStackEntry?.destination?.route
    } ?: Screen.Accounts.List

    Scaffold(
        topBar = {
            WalletAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {
                    navController.navigateUp()

                    /*if (accountsViewModel.canExitForm()) {
                        navController.navigateUp()
                    }*/
                }
            )
        },
        bottomBar = {
            WalletNavigationBar(
                items = screens,
                navController = navController,
                currentDestination = backStackEntry?.destination
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = screens[0].route,
            modifier = Modifier.padding(innerPadding)
        ) {
            accountsGraph(
                route = screens[0].route,
                navController = navController
            )

            categoriesGraph(
                route = screens[1].route,
                navController = navController
            )
        }
    }
}