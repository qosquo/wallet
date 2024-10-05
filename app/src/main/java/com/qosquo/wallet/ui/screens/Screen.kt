package com.qosquo.wallet.ui.screens

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.qosquo.wallet.R
import kotlinx.serialization.Serializable

sealed class Screens {
    @Serializable
    sealed class Accounts(val title: String) {
        @Serializable
        data object List : Accounts("Accounts")
        @Serializable
        data object Form : Accounts("Edit account")
    }
    @Serializable
    sealed class Categories(val title: String) {
        @Serializable
        data object List : Categories("Categories")
    }
}

sealed class Routes {
    @Serializable
    object Accounts
    @Serializable
    object Categories
}

sealed class Screen (
    open val route: String,
    @StringRes open val resourceId: Int
) {

    // Screen routes
    sealed class Accounts() : Screen(
//        R.drawable.bank_fill,
        "accounts",
        R.string.accounts
    ) {
        data object List : Screen("accounts/list", R.string.accounts_list)
        data object Create : Screen("accounts/create", R.string.accounts_create)
        data object Edit : Screen("accounts/edit", R.string.accounts_edit)
    }

    sealed class Categories() : Screen(
//        R.drawable.chart_bar_fill,
        "categories",
        R.string.categories
    ) {
        data object List : Screen("categories/list", R.string.categories_list)
    }

    // Graph routes
    data object AccountsNav : Screen("ACCOUNTS_NAV_GRAPH", R.string.accounts)
    data object CategoriesNav : Screen("CATEGORIES_NAV_GRAPH", R.string.categories)
}

data class TopLevelRoute<T : Any>(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: T
)