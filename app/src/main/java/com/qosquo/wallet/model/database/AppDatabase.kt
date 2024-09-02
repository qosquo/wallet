package com.qosquo.wallet.model.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.qosquo.wallet.model.database.entities.AccountsDbEntity
import com.qosquo.wallet.model.database.entities.CategoriesDbEntity
import com.qosquo.wallet.model.database.entities.TransactionsDbEntity

@Database(
    version = 5,
    entities = [
        AccountsDbEntity::class,
        CategoriesDbEntity::class,
        TransactionsDbEntity::class
    ],
    autoMigrations = [
        AutoMigration(
            from = 4,
            to = 5,
//            spec = AppDatabase.AccountIconMigration::class
        )
    ]
)
abstract class AppDatabase : RoomDatabase() {
    @RenameColumn(
        tableName = "accounts",
        fromColumnName = "account_icon_path",
        toColumnName = "account_icon_id"
        )
    class AccountIconMigration : AutoMigrationSpec

    abstract fun getAccountsDao(): AccountsDao

    abstract fun getTransactionsDao(): TransactionsDao

}