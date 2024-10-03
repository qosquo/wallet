package com.qosquo.wallet

import android.content.Context
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.qosquo.wallet.model.database.AppDatabase
import com.qosquo.wallet.viewmodel.AccountsViewModel
import com.qosquo.wallet.viewmodel.CategoriesViewModel

object Dependencies {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
//        _appDatabase.clearAllTables()
    }

    private val MIGRATION_6_7 = object : Migration(6,7) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE 'categories' ('color_hex' TEXT NOT NULL, 'category_icon_id' INTEGER NOT NULL, " +
                    "'id' INTEGER NOT NULL, 'category_name' TEXT NOT NULL, " +
                    "'goal' REAL NOT NULL, 'type' INTEGER NOT NULL, " +
                    "PRIMARY KEY('id'))")
            db.execSQL("DROP TABLE 'transactions';")
            db.execSQL("CREATE TABLE 'transactions' ('id' INTEGER NOT NULL, 'amount' REAL NOT NULL, " +
                    "'account_id' INTEGER NOT NULL, 'category_id' INTEGER NOT NULL, " +
                    "'date' INTEGER NOT NULL, 'notes' TEXT NOT NULL, " +
                    "PRIMARY KEY('id'), " +
                    "FOREIGN KEY ('account_id') REFERENCES accounts('id'), " +
                    "FOREIGN KEY ('category_id') REFERENCES categories('id')" +
                    ");")
            db.execSQL("CREATE INDEX 'index_transactions_id' ON transactions('id')")
        }
    }

    private val _appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database_test.db"
        ).addMigrations(MIGRATION_6_7).allowMainThreadQueries().build()
    }

    val accountsViewModel by lazy {
        AccountsViewModel(_appDatabase.getAccountsDao())
    }
    val categoriesViewModel by lazy {
        CategoriesViewModel(_appDatabase.getCategoriesDao())
    }

}