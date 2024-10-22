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

/**
 * Return signed value based on category type.
 * @property categoryType 0 - 'expense', 1 - 'income'
 * @property amount amount which value must return signed
 * @return Value is negative when category type is 'expense' and positive, when 'income'
 */
fun signedValue(categoryType: Int, amount: Float) : Float {
    val sign = if (categoryType == 0) {
        // expense
        -1
    } else {
        // income
        1
    }
    return amount * sign
}