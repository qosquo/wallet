package com.qosquo.wallet.data.db.entity

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
)
data class CategoriesDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "category_name") val categoryName: String,
    val type: Int,
    val goal: Float,
    @ColumnInfo(name = "category_icon_id") @DrawableRes val categoryIconId: Int,
    @ColumnInfo(name = "color_hex") val colorHex: String
)
