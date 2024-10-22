package com.qosquo.wallet.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screens {
    @Serializable
    sealed class Operations {
        @Serializable
        object List
        @Serializable
        data class Form(val transactionId: Long?)
        @Serializable
        object AccountSelection
        @Serializable
        object CategorySelection
    }
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
        data class Form(val categoryId: Long?, val type: Int)
    }
}