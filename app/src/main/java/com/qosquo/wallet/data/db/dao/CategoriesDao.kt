package com.qosquo.wallet.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.qosquo.wallet.domain.Category
import com.qosquo.wallet.data.db.entity.CategoriesDbEntity

@Dao
interface CategoriesDao {

    @Upsert(entity = CategoriesDbEntity::class)
    fun upsertNewCategoryData(category: CategoriesDbEntity)

    @Query("SELECT categories.id, categories.category_name AS name," +
            "categories.type, categories.goal, categories.category_icon_id AS iconId," +
            "categories.color_hex AS colorHex FROM categories")
    fun getAllCategoriesData(): List<Category>

    @Query("SELECT categories.id, categories.category_name AS name," +
            "categories.type, categories.goal, categories.category_icon_id AS iconId," +
            "categories.color_hex AS colorHex FROM categories " +
            "WHERE categories.type = :typeId")
    fun getCategoriesOfType(typeId: Short): List<Category>

    @Query("SELECT categories.id, categories.category_name AS name," +
            "categories.type, categories.goal, categories.category_icon_id AS iconId," +
            "categories.color_hex AS colorHex FROM categories WHERE id = :categoryId LIMIT 1")
    fun getCategoryFromId(categoryId: Long): Category

    @Query("DELETE FROM categories WHERE ID = :categoryId")
    fun deleteCategoryDataById(categoryId: Long)

}