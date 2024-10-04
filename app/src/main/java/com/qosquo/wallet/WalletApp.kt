package com.qosquo.wallet

import android.media.Image
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.qosquo.wallet.ui.screens.BottomNavigationItem
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
fun WalletApp(
    navController: NavHostController = rememberNavController()
) {
    val routes = listOf(
        Screen.Accounts.List,
        Screen.Accounts.Create,
        Screen.Accounts.Edit,

        Screen.Categories.List
    )
    val navItems = listOf(
        BottomNavigationItem(
            title = "Accounts",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.bank_fill),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.bank_line),
            route = Screen.AccountsNav.route,
        ),
        BottomNavigationItem(
            title = "Categories",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.chart_bar_fill),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.chart_bar_line),
            route = Screen.CategoriesNav.route,
        ),
    )

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = routes.find {
        it.route == backStackEntry?.destination?.route
    } ?: Screen.Accounts.List

    val prevRoute: String? = navController.previousBackStackEntry?.destination?.route
    val prevRouteSplit = prevRoute?.split('/')?.get(0)

    var selectedNavItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
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
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        label = {
                            Text(text = item.title)
                        },
                        selected = selectedNavItemIndex == index,
                        onClick = {
                            selectedNavItemIndex = index

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
                                imageVector = if (selectedNavItemIndex == index) {
                                    item.selectedIcon
                                } else {
                                    item.unselectedIcon
                                },
                                contentDescription = item.title
                            )
                        }
                    )
                }
            }
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