package com.qosquo.wallet.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import com.qosquo.wallet.R

sealed class Screens (
    @DrawableRes val iconId: Int,
    open val route: String,
    @StringRes open val resourceId: Int
) {
    sealed class Accounts(
        override val route: String,
        @StringRes override val resourceId: Int
    ) : Screens(
        R.drawable.bank_fill,
        "accounts",
        R.string.accounts
    ) {
        data object List : Accounts("list", R.string.accounts_list)
        data object Create : Accounts("create", R.string.accounts_create)
        data object Edit : Accounts("edit", R.string.accounts_edit)
    }

    sealed class Categories(
        override val route: String,
        @StringRes override val resourceId: Int
    ) : Screens(
        R.drawable.chart_bar_fill,
        "categories",
        R.string.categories
    ) {
        data object List : Categories("list", R.string.categories_list)
    }
}