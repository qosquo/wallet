package com.qosquo.wallet.model.database.entities

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
)
data class CategoriesDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "category_name") val categoryName: String,
    val type: Short,
    val goal: Float,
    @ColumnInfo(name = "category_icon_id") @DrawableRes val categoryIconId: Int,
    @ColumnInfo(name = "color_hex") val colorHex: String
)
