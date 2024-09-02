package com.qosquo.wallet

import android.content.Context
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.qosquo.wallet.model.database.AppDatabase
import com.qosquo.wallet.viewmodel.AccountsViewModel

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
            "database_test.db"
        ).allowMainThreadQueries().build()
    }

    val accountsViewModel by lazy {
        AccountsViewModel(_appDatabase.getAccountsDao())
    }

}