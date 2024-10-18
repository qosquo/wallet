package com.qosquo.wallet.data.db.database

import androidx.room.ColumnInfo

data class TransactionsInfoTuple(
    val id: Long,
    val amount: Float,
    @ColumnInfo(name = "account_name") val accountName: String,
    @ColumnInfo(name = "category_name") val categoryName: String,
    val date: Long,
    val notes: String
)