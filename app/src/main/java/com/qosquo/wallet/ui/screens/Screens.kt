package com.qosquo.wallet.ui.screens

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

sealed interface Screens {
    @Serializable
    sealed class Accounts {
        @Serializable
        object List
        @Serializable
        data class Form(val accountId: Long?)
    }
    @Serializable
    sealed class Categories {
        @Serializable
        object List
        @Serializable
        data class Form(val categoryId: Long?)
    }
}

sealed interface Routes {
    @Serializable
    object Accounts
    @Serializable
    object Categories
}

data class TopLevelRoute<T : Any>(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: T
)