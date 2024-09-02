package com.qosquo.wallet.model.database.entities

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "accounts",

)
data class AccountsDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val balance: Float,
    @ColumnInfo(name = "account_name") val accountName: String,
    @ColumnInfo(name = "account_icon_id") @DrawableRes val accountIconId: Int,
    @ColumnInfo(name = "color_hex") val colorHex: String,
    val count: Boolean
)
