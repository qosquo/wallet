package com.qosquo.wallet.domain

import androidx.annotation.DrawableRes
import com.qosquo.wallet.R

enum class Currencies(
    @DrawableRes val id: Int,
    val symbol: Char,
    val isLeftSide: Boolean
) {
    BITCOIN(id = R.drawable.currency_bitcoin_line, symbol = '₿', isLeftSide = false),
    DOLLAR(id = R.drawable.currency_dollar_line, symbol = '$', isLeftSide = true),
    EURO(id = R.drawable.currency_euro_line, symbol = '€', isLeftSide = true),
    POUND(id = R.drawable.currency_pound_line, symbol = '£', isLeftSide = true),
    RUBEL(id = R.drawable.currency_rubel_line, symbol = '₽', isLeftSide = false)
}

data class Account(
    val id: Long,
    val balance: Float,
    val currency: Int,
    val name: String,
    @DrawableRes val accountIconId: Int,
    val colorHex: String,
    val count: Boolean
)
