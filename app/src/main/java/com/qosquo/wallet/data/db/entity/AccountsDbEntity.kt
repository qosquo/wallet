package com.qosquo.wallet.data.db.entity

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "accounts",

)
data class AccountsDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val balance: Float,
    val currency: Int,
    @ColumnInfo(name = "account_name") val accountName: String,
    @ColumnInfo(name = "account_icon_id") @DrawableRes val accountIconId: Int,
    @ColumnInfo(name = "color_hex") val colorHex: String,
    val count: Boolean
)
