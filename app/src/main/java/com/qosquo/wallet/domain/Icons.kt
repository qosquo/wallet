package com.qosquo.wallet.domain

import androidx.annotation.DrawableRes
import com.qosquo.wallet.R

sealed interface Icons {
    enum class Accounts(@DrawableRes val id: Int): Icons {
        BANK(id = R.drawable.bank_line),
        BANK_CARD(R.drawable.bank_card_line),
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

    enum class Categories(@DrawableRes val id: Int) : Icons {
        UNKNOWN(id = R.drawable.question_line),
        HOME(id = R.drawable.home_3_line),
        BED(id = R.drawable.bed_line),
        SOFA(id = R.drawable.sofa_line),
        BULB(id = R.drawable.bulb_2_line),
        TOILET_PAPER(id = R.drawable.toilet_paper_line),
        BUS(id = R.drawable.bus_line),
        HAMBURGER(id = R.drawable.hamburger_line),
        BABY(id = R.drawable.baby_line),
        PARKING(id = R.drawable.parking_line),
        CAR(id = R.drawable.car_line),
    }
}