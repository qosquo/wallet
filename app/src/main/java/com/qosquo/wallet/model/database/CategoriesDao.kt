package com.qosquo.wallet.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.qosquo.wallet.model.database.entities.AccountsDbEntity
import com.qosquo.wallet.model.database.entities.CategoriesDbEntity

@Dao
interface CategoriesDao {

    @Insert(entity = CategoriesDbEntity::class)
    fun insertNewCategoryData(category: CategoriesDbEntity)

    @Query("SELECT * FROM categories")
    fun getAllCategoriesData(): List<CategoriesDbEntity>

    @Query("DELETE FROM categories WHERE ID = :categoryId")
    fun deleteCategoryDataById(categoryId: Long)

}