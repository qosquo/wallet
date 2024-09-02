package com.qosquo.wallet.ui

import androidx.annotation.DrawableRes
import com.qosquo.wallet.R

sealed interface Icons {
    enum class Accounts(@DrawableRes val id: Int): Icons {
        BANK(id = R.drawable.bank),
        BANK_CARD(R.drawable.bank_card),
        CASH(R.drawable.cash),
        CURRENCY_BITCOIN(R.drawable.currency_bitcoin),
        CURRENCY_DOLLAR(R.drawable.currency_dollar),
        CURRENCY_EURO(R.drawable.currency_euro),
        CURRENCY_POUND(R.drawable.currency_pound),
        PAYPAL(R.drawable.paypal),
        PIG_MONEY(R.drawable.pig_money),
        SAFE_BOX(R.drawable.safe_box),
        WALLET(R.drawable.wallet)
    }
}