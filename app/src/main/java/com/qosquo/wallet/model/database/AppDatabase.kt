package com.qosquo.wallet.model.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.qosquo.wallet.model.database.entities.AccountsDbEntity
import com.qosquo.wallet.model.database.entities.CategoriesDbEntity
import com.qosquo.wallet.model.database.entities.TransactionsDbEntity

@Database(
    version = 8,
    entities = [
        AccountsDbEntity::class,
        CategoriesDbEntity::class,
        TransactionsDbEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAccountsDao(): AccountsDao
    abstract fun getCategoriesDao(): CategoriesDao
    abstract fun getTransactionsDao(): TransactionsDao

}