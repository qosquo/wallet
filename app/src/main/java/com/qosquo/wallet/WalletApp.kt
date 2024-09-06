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
import androidx.compose.ui.res.painterResource
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
import com.qosquo.wallet.ui.screens.BottomNavItems
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
    navController: NavController,
) {
    val items = listOf(
        BottomNavItems.AccountsItem,
        BottomNavItems.CategoriesItem
    )
    BottomNavigation(
        modifier = Modifier.navigationBarsPadding()
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            BottomNavigationItem(
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
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
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.title),
                    )
                },
                label = { Text(text = stringResource(id = item.title)) }
            )
        }
    }
}

@Composable
fun WalletApp(
    navController: NavHostController = rememberNavController()
) {
    val routes = listOf(
        Screen.Accounts.List,
        Screen.Accounts.Create,
        Screen.Accounts.Edit,

        Screen.Categories.List
    )

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = routes.find {
        it.route == backStackEntry?.destination?.route
    } ?: Screen.Accounts.List

    val prevRoute: String? = navController.previousBackStackEntry?.destination?.route
    val prevRouteSplit = prevRoute?.split('/')?.get(0)

    Scaffold(
        topBar = {
            WalletAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null &&
                    navController.previousBackStackEntry?.destination?.route
                        ?.split('/')?.get(0) == currentScreen.route.split('/')[0],
                navigateUp = {
                    navController.navigateUp()
                }
            )
        },
        bottomBar = {
            WalletNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.AccountsNav.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            accountsGraph(
                route = Screen.AccountsNav.route,
                navController = navController
            )

            categoriesGraph(
                route = Screen.CategoriesNav.route,
                navController = navController
            )
        }
    }
}