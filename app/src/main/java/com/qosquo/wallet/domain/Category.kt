package com.qosquo.wallet.domain

import androidx.annotation.DrawableRes

data class Category(
    val id: Long,
    val name: String,
    val type: Int,  // 0 - expense, 1 - income
    val goal: Float,
    @DrawableRes val iconId: Int,
    val colorHex: String
)