package com.qosquo.wallet.utils

import com.qosquo.wallet.domain.Currencies
import kotlin.math.abs

fun amountToStringWithCurrency(amount: Float, currency: Currencies): String {
    val sign: String = if (amount < 0) {
        "-"
    } else {
        ""
    }
    return if (currency.isLeftSide) {
        "$sign${currency.symbol}${abs(amount).toInt()}"
    } else {
        "$sign${abs(amount).toInt()}${currency.symbol}"
    }
}