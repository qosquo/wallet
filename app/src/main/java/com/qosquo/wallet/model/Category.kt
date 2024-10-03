package com.qosquo.wallet.model

import androidx.annotation.DrawableRes

enum class CategoryTypes(val id: Short) {
    EXPENSES(0),
    INCOME(1),
    TRANSFER(2)
}

data class Category(
    val id: Long,
    val name: String,
    val type: Short,
    val goal: Float,
    @DrawableRes val iconId: Int,
    val colorHex: String
)