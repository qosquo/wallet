package com.qosquo.wallet.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.qosquo.wallet.data.db.dao.AccountsDao
import com.qosquo.wallet.data.db.dao.CategoriesDao
import com.qosquo.wallet.data.db.dao.TransactionsDao
import com.qosquo.wallet.data.db.entity.AccountsDbEntity
import com.qosquo.wallet.data.db.entity.CategoriesDbEntity
import com.qosquo.wallet.data.db.entity.TransactionsDbEntity

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