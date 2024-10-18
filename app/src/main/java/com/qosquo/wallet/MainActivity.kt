package com.qosquo.wallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.qosquo.wallet.Dependencies.categoriesViewModel
import com.qosquo.wallet.presentation.navigation.Routes
import com.qosquo.wallet.presentation.navigation.Screens
import com.qosquo.wallet.presentation.navigation.TopLevelRoute
import com.qosquo.wallet.presentation.ui.accounts.AccountsForm
import com.qosquo.wallet.presentation.ui.accounts.AccountsList
import com.qosquo.wallet.presentation.ui.categories.CategoriesForm
import com.qosquo.wallet.presentation.ui.categories.CategoriesList
import com.qosquo.wallet.presentation.ui.theme.WalletTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Dependencies.init(applicationContext)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WalletTheme {
                val navController: NavHostController = rememberNavController()
                val navItems = listOf(
                    TopLevelRoute(
                        title = "Accounts",
                        selectedIcon = ImageVector.vectorResource(id = R.drawable.bank_fill),
                        unselectedIcon = ImageVector.vectorResource(id = R.drawable.bank_line),
                        route = Routes.Accounts,
                    ),
                    TopLevelRoute(
                        title = "Categories",
                        selectedIcon = ImageVector.vectorResource(id = R.drawable.chart_bar_fill),
                        unselectedIcon = ImageVector.vectorResource(id = R.drawable.chart_bar_line),
                        route = Routes.Categories,
                    ),
                )

                // Get current back stack entry
                val backStackEntry by navController.currentBackStackEntryAsState()
                var selectedNavItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.Accounts,
                        modifier = Modifier.weight(1f)
                    ) {
                        navigation<Routes.Accounts> (
                            startDestination = Screens.Accounts.List,
                        ) {
                            composable<Screens.Accounts.List> {
                                AccountsList(
                                    onNavigate = { id ->
                                        navController.navigate(
                                            Screens.Accounts.Form(
                                                accountId = id
                                            ))
                                    }
                                )
                            }

                            composable<Screens.Accounts.Form> {
                                val id: Long? = it.toRoute<Screens.Accounts.Form>().accountId
                                AccountsForm(
                                    accountId = id,
                                    onEvent = Dependencies.accountsViewModel::onAction,
                                    onNavigate = {
                                        navController.navigateUp()
                                    }
                                )
                            }
                        }

                        navigation<Routes.Categories>(
                            startDestination = Screens.Categories.List,
                        ) {
                            composable<Screens.Categories.List> {
                                CategoriesList(
                                    onTabChange = categoriesViewModel::onAction,
                                    onNavigate = { id ->
                                        navController.navigate(
                                            Screens.Categories.Form(
                                                categoryId = id
                                            ))
                                    }
                                )
                            }

                            composable<Screens.Categories.Form> {
                                val id: Long? = it.toRoute<Screens.Categories.Form>().categoryId
                                CategoriesForm(
                                    categoryId = id,
                                    onEvent = categoriesViewModel::onAction,
                                    onNavigate = {
                                        navController.navigateUp()
                                    }
                                )
                            }
                        }
                    }

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
            }
        }
    }
}

