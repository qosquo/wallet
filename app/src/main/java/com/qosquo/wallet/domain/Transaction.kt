package com.qosquo.wallet.domain

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo

data class Transaction(
    val id: Long,
    val amount: Float,
    @ColumnInfo(name = "account_id") val accountId: Long,
    @ColumnInfo(name = "account_name") val accountName: String,
    @ColumnInfo(name = "account_icon_id") @DrawableRes val accountIconId: Int,
    @ColumnInfo(name = "account_color_hex") val accountColorHex: String,
    @ColumnInfo(name = "account_currency") val accountCurrency: Int,
    @ColumnInfo(name = "category_id") val categoryId: Long,
    @ColumnInfo(name = "category_name") val categoryName: String,
    @ColumnInfo(name = "category_type") val categoryTypeId: Int,
    @ColumnInfo(name = "category_icon_id") @DrawableRes val categoryIconId: Int,
    @ColumnInfo(name = "category_color_hex") val categoryColorHex: String,
    val date: Long,
    val notes: String
)

data class TransactionAccount(
    val id: Long,
    val name: String,
    val currency: Int,
    @ColumnInfo(name = "color_hex") val colorHex: String,
    @ColumnInfo(name = "icon_id") @DrawableRes val iconId: Int
)

data class TransactionCategory(
    val id: Long,
    val name: String,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "color_hex") val colorHex: String,
    @ColumnInfo(name = "icon_id") @DrawableRes val iconId: Int
)