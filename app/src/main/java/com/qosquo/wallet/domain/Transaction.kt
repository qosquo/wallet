package com.qosquo.wallet.domain

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo

data class Transaction(
    val id: Long,
    val amount: Float,
    @ColumnInfo(name = "account_name") val accountName: String,
    @ColumnInfo(name = "currency") val accountCurrency: Int,
    @ColumnInfo(name = "category_name") val categoryName: String,
    @ColumnInfo(name = "category_icon_id") @DrawableRes val categoryIconId: Int,
    @ColumnInfo(name = "color_hex") val colorHex: String,
    val date: Long,
    val notes: String
)