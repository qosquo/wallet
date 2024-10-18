package com.qosquo.wallet

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.qosquo.wallet.Dependencies.accountsViewModel
import com.qosquo.wallet.Dependencies.categoriesViewModel
import com.qosquo.wallet.presentation.navigation.TopLevelRoute
import com.qosquo.wallet.presentation.navigation.Routes
import com.qosquo.wallet.presentation.navigation.Screens
import com.qosquo.wallet.presentation.ui.accounts.AccountsForm
import com.qosquo.wallet.presentation.ui.accounts.AccountsList
import com.qosquo.wallet.presentation.ui.categories.CategoriesForm
import com.qosquo.wallet.presentation.ui.categories.CategoriesList

fun NavBackStackEntry?.fromRoute(): String? {
    return this?.destination?.route?.substringBefore("?")?.substringBefore("/")
}

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletApp(
    navController: NavHostController = rememberNavController()
) {
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title: Int? = backStackEntry?.fromRoute().let {
                            when (it) {
                                Screens.Accounts.List::class.qualifiedName ->
                                    R.string.accounts_list
                                Screens.Accounts.Form::class.qualifiedName ->
                                    R.string.accounts_form
                                Screens.Categories.List::class.qualifiedName ->
                                    R.string.categories_list
                                Screens.Categories.Form::class.qualifiedName ->
                                    R.string.categories_form
                                else -> { null }
                            }
                        }
                    title?.let { Text(stringResource(id = it)) }
                },
                navigationIcon = {
                    val canNavigateBack: Boolean = backStackEntry?.fromRoute().let {
                        when (it) {
                            Screens.Accounts.Form::class.qualifiedName ->
                                true
                            Screens.Categories.Form::class.qualifiedName ->
                                true
                            else -> { false }
                        }
                    }
                    if (canNavigateBack) {
                        IconButton(onClick = {
                            navController.navigateUp()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "back button"
                            )
                        }
                    }
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
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Accounts,
            modifier = Modifier.padding(innerPadding)
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
                        onEvent = accountsViewModel::onAction,
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
    }
}