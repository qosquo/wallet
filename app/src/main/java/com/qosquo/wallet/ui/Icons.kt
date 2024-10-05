package com.qosquo.wallet.ui

import androidx.annotation.DrawableRes
import com.qosquo.wallet.R

sealed interface Icons {
    enum class Accounts(@DrawableRes val id: Int): Icons {
        BANK(id = R.drawable.bank_line),
        BANK_CARD(R.drawable.bank_card),
        CASH(R.drawable.cash_line),
        CURRENCY_BITCOIN(R.drawable.currency_bitcoin_line),
        CURRENCY_DOLLAR(R.drawable.currency_dollar_line),
        CURRENCY_EURO(R.drawable.currency_euro_line),
        CURRENCY_POUND(R.drawable.currency_pound_line),
        PAYPAL(R.drawable.paypal_line),
        PIG_MONEY(R.drawable.pig_money_line),
        SAFE_BOX(R.drawable.safe_box_line),
        WALLET(R.drawable.wallet_line)
    }
}