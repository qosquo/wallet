package com.qosquo.wallet.domain

import androidx.annotation.DrawableRes

enum class CategoryTypes() {
    EXPENSES,
    INCOME,
    TRANSFER
}

data class Category(
    val id: Long,
    val name: String,
    val type: Int,
    val goal: Float,
    @DrawableRes val iconId: Int,
    val colorHex: String
)