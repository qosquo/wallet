package com.qosquo.wallet.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    indices = [Index("id")],
    foreignKeys = [
        ForeignKey(
            entity = AccountsDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["account_id"]
        ),
        ForeignKey(
            entity = CategoriesDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"]
        )
    ]
)
data class TransactionsDbEntity (
    @PrimaryKey(autoGenerate = true) val id: Long,
    val amount: Float,
    @ColumnInfo(name = "account_id") val accountId: Long,
    @ColumnInfo(name = "category_id") val categoryId: Long,
    val date: Long,
    val notes: String
)