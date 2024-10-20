package com.qosquo.wallet

import android.content.Context
import androidx.room.Room
import com.qosquo.wallet.data.db.database.AppDatabase
import com.qosquo.wallet.data.db.migration.MIGRATION_6_7
import com.qosquo.wallet.data.db.migration.MIGRATION_7_8
import com.qosquo.wallet.presentation.ui.accounts.AccountsViewModel
import com.qosquo.wallet.presentation.ui.categories.CategoriesViewModel
import com.qosquo.wallet.presentation.ui.operations.TransactionViewModel

object Dependencies {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
//        _appDatabase.clearAllTables()
    }

    private val _appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database.db"
        )
            .addMigrations(MIGRATION_6_7)
            .addMigrations(MIGRATION_7_8)
            .allowMainThreadQueries()
            .build()
    }

    val accountsViewModel by lazy {
        AccountsViewModel(_appDatabase.getAccountsDao())
    }
    val categoriesViewModel by lazy {
        CategoriesViewModel(_appDatabase.getCategoriesDao())
    }
    val transactionsViewModel by lazy {
        TransactionViewModel(_appDatabase.getTransactionsDao())
    }

}