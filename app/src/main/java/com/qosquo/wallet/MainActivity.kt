package com.qosquo.wallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.scaleIn
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
import com.qosquo.wallet.presentation.ui.operations.OperationsList
import com.qosquo.wallet.presentation.ui.theme.WalletTheme
import com.qosquo.wallet.ui.screens.operations.AccountsSelection
import com.qosquo.wallet.ui.screens.operations.CategoriesSelection
import com.qosquo.wallet.ui.screens.operations.OperationsForm
import com.qosquo.wallet.ui.screens.operations.OperationsNavigateCode
import com.qosquo.wallet.utils.fromRoute

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
                        title = "Operations",
                        selectedIcon = ImageVector.vectorResource(id = R.drawable.home_3_fill),
                        unselectedIcon = ImageVector.vectorResource(id = R.drawable.home_3_line),
                        route = Routes.Operations,
                    ),
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
                        startDestination = Routes.Operations,
                        modifier = Modifier.weight(1f)
                    ) {
                        navigation<Routes.Operations> (
                            startDestination = Screens.Operations.List,
                        ) {
                            composable<Screens.Operations.List>(
                                enterTransition = {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                },
                                exitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                },
                                popEnterTransition = {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                },
                                popExitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                }
                            ) {
                                OperationsList(
                                    navigate = { transactionId, accountId, categoryId ->
                                        navController.navigate(Screens.Operations.Form(transactionId = transactionId))
                                        navController.currentBackStackEntry
                                            ?.savedStateHandle
                                            ?.set("selectedAccountId", accountId)
                                        navController.currentBackStackEntry
                                            ?.savedStateHandle
                                            ?.set("selectedCategoryId", categoryId)
                                    }
                                )
                            }

                            composable<Screens.Operations.Form>(
                                enterTransition = {
                                    when (initialState.fromRoute()) {
                                        Screens.Operations.List::class.qualifiedName ->
                                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                        Screens.Operations.AccountSelection::class.qualifiedName,
                                        Screens.Operations.CategorySelection::class.qualifiedName ->
                                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                        else -> null
                                    }
                                },
                                exitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                },
                                popEnterTransition = {
                                    when (initialState.fromRoute()) {
                                        Screens.Operations.List::class.qualifiedName ->
                                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                        Screens.Operations.AccountSelection::class.qualifiedName,
                                        Screens.Operations.CategorySelection::class.qualifiedName ->
                                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                        else -> null
                                    }
                                },
                                popExitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                }
                            ) {
                                val id: Long? = it.toRoute<Screens.Operations.Form>().transactionId
                                OperationsForm(
                                    transactionId = id,
                                    accountId = it.savedStateHandle["selectedAccountId"],
                                    categoryId = it.savedStateHandle["selectedCategoryId"],
                                    onAction = Dependencies.transactionsViewModel::onAction,
                                    navigate = { code ->
                                        when (code) {
                                            OperationsNavigateCode.BACK -> {
                                                navController.navigateUp()
                                                navController.currentBackStackEntry
                                                    ?.savedStateHandle
                                                    ?.set("selectedAccountId", null)
                                                navController.currentBackStackEntry
                                                    ?.savedStateHandle
                                                    ?.set("selectedCategoryId", null)
                                            }
                                            OperationsNavigateCode.ACCOUNT -> {
                                                navController.navigate(Screens.Operations.AccountSelection)
                                            }
                                            OperationsNavigateCode.CATEGORY -> {
                                                navController.navigate(Screens.Operations.CategorySelection)
                                            }
                                        }
                                    }
                                )
                            }

                            composable<Screens.Operations.AccountSelection>(
                                enterTransition = {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                },
                                exitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                },
                                popEnterTransition = {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                },
                                popExitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                }
                            ) {
                                AccountsSelection(
                                    onSelect = { accountId ->
                                        navController.navigateUp()
                                        if (accountId != null) {
                                            navController.currentBackStackEntry
                                                ?.savedStateHandle
                                                ?.set("selectedAccountId", accountId)
                                        }
                                    }
                                )
                            }

                            composable<Screens.Operations.CategorySelection>(
                                enterTransition = {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                },
                                exitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                },
                                popEnterTransition = {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                },
                                popExitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                }
                            ) {
                                CategoriesSelection(
                                    onTabChange = Dependencies.categoriesViewModel::onAction,
                                    onSelect = { categoryId ->
                                        navController.navigateUp()
                                        if (categoryId != null) {
                                            navController.currentBackStackEntry
                                                ?.savedStateHandle
                                                ?.set("selectedCategoryId", categoryId)
                                        }
                                    }
                                )
                            }
                        }

                        navigation<Routes.Accounts> (
                            startDestination = Screens.Accounts.List,
                        ) {
                            composable<Screens.Accounts.List>(
                                enterTransition = {
                                    when (initialState.fromRoute()) {
                                        Screens.Operations.List::class.qualifiedName ->
                                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                        Screens.Categories.List::class.qualifiedName ->
                                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                        Screens.Accounts.Form::class.qualifiedName ->
                                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                        else -> null
                                    }
                                },
                                exitTransition = {
                                    when (targetState.fromRoute()) {
                                        Screens.Operations.List::class.qualifiedName ->
                                            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                        Screens.Categories.List::class.qualifiedName ->
                                            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                        Screens.Accounts.Form::class.qualifiedName ->
                                            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                        else -> null
                                    }
                                },
                                popEnterTransition = {
                                    when (initialState.fromRoute()) {
                                        Screens.Operations.List::class.qualifiedName ->
                                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                        Screens.Categories.List::class.qualifiedName ->
                                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                        Screens.Accounts.Form::class.qualifiedName ->
                                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                        else -> null
                                    }
                                },
                                popExitTransition = {
                                    when (targetState.fromRoute()) {
                                        Screens.Operations.List::class.qualifiedName ->
                                            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                        Screens.Categories.List::class.qualifiedName ->
                                            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                        Screens.Accounts.Form::class.qualifiedName ->
                                            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                        else -> null
                                    }
                                }
                            ) {
                                AccountsList(
                                    onNavigate = { id ->
                                        navController.navigate(
                                            Screens.Accounts.Form(
                                                accountId = id
                                            ))
                                    }
                                )
                            }

                            composable<Screens.Accounts.Form>(
                                enterTransition = {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                },
                                exitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                },
                                popEnterTransition = {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                },
                                popExitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                }
                            ) {
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
                            composable<Screens.Categories.List>(
                                enterTransition = {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                },
                                exitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                },
                                popEnterTransition = {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                },
                                popExitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                }
                            ) {
                                CategoriesList(
                                    onTabChange = categoriesViewModel::onAction,
                                    onNavigate = { id, typeId ->
                                        navController.navigate(
                                            Screens.Categories.Form(
                                                categoryId = id,
                                                typeId = typeId
                                            ))
                                    }
                                )
                            }

                            composable<Screens.Categories.Form>(
                                enterTransition = {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                },
                                exitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                },
                                popEnterTransition = {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                                },
                                popExitTransition = {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                                }
                            ) {
                                val id: Long? = it.toRoute<Screens.Categories.Form>().categoryId
                                val typeId: Int = it.toRoute<Screens.Categories.Form>().typeId
                                CategoriesForm(
                                    categoryId = id,
                                    typeId = typeId,
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

