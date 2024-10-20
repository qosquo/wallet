package com.qosquo.wallet.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    object Operations
    @Serializable
    object Accounts
    @Serializable
    object Categories
}