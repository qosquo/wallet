package com.qosquo.wallet.model.database

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class TransactionsInfoTuple(
    val id: Long,
    val amount: Float,
    @ColumnInfo(name = "account_name") val accountName: String,
    @ColumnInfo(name = "category_name") val categoryName: String,
    val date: Long,
    val notes: String
)