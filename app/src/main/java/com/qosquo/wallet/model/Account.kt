package com.qosquo.wallet.model

import androidx.annotation.DrawableRes
import com.qosquo.wallet.model.database.entities.AccountsDbEntity

enum class Currencies {
    DOLLAR,
    EURO,
    RUBEL,
    POUND,
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
