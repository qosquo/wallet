package com.qosquo.wallet.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.qosquo.wallet.R

sealed class Screen (
    @DrawableRes val iconId: Int,
    open val route: String,
    @StringRes open val resourceId: Int
) {

    sealed class Accounts(
        override val route: String,
        @StringRes override val resourceId: Int
    ) : Screen(
        R.drawable.bank_fill,
        "accounts",
        R.string.accounts
    ) {
        data object Root : Accounts("accounts", R.string.accounts)
        data object List : Accounts("accounts/list", R.string.accounts_list)
        data object Create : Accounts("accounts/create", R.string.accounts_create)
        data object Edit : Accounts("accounts/edit", R.string.accounts_edit)
    }

    sealed class Categories(
        override val route: String,
        @StringRes override val resourceId: Int
    ) : Screen(
        R.drawable.chart_bar_fill,
        "categories",
        R.string.categories
    ) {
        data object Root : Categories("categories", R.string.categories)
        data object List : Categories("categories/list", R.string.categories_list)
    }
}