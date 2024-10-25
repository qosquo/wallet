package com.qosquo.wallet

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qosquo.wallet.data.db.dao.AccountsDao
import com.qosquo.wallet.data.db.dao.CategoriesDao
import com.qosquo.wallet.data.db.dao.TransactionsDao
import com.qosquo.wallet.data.db.database.AppDatabase
import com.qosquo.wallet.data.db.entity.AccountsDbEntity
import com.qosquo.wallet.data.db.entity.CategoriesDbEntity
import com.qosquo.wallet.data.db.entity.TransactionsDbEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DeleteAccountTest {
    private lateinit var accountsDao: AccountsDao
    private lateinit var categoriesDao: CategoriesDao
    private lateinit var transactionsDao: TransactionsDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        accountsDao = db.getAccountsDao()
        categoriesDao = db.getCategoriesDao()
        transactionsDao = db.getTransactionsDao()
    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun deleteAccountWithTransactions() {
        // add test account to database
        accountsDao.upsertNewAccountData(
            AccountsDbEntity(
                id = 0,
                accountName = "test account",
                accountIconId = 0,
                count = true,
                colorHex = "#000000",
                currency = 3,
                balance = 500F
            )
        )
        // find test account id
        val accounts = accountsDao.getAllAccountsData()
        assert(accounts.size == 1)

        // insert test category
        categoriesDao.upsertNewCategoryData(
            CategoriesDbEntity(
                id = 0,
                categoryName = "Other",
                colorHex = "#FFFFFF",
                goal = 0F,
                type = 0,
                categoryIconId = 0
            )
        )
        val categories = categoriesDao.getAllCategoriesData()
        assert(categories.size == 1)

        // insert some transactions with test account and category
        transactionsDao.upsertNewTransaction(
            TransactionsDbEntity(
                id = 0,
                accountId = 1,
                categoryId = 1,
                date = 0L,
                notes = "",
                amount = 200.0f
            )
        )
        transactionsDao.upsertNewTransaction(
            TransactionsDbEntity(
                id = 0,
                accountId = 1,
                categoryId = 1,
                date = 0L,
                notes = "",
                amount = 300.0f
            )
        )
        transactionsDao.upsertNewTransaction(
            TransactionsDbEntity(
                id = 0,
                accountId = 1,
                categoryId = 1,
                date = 0L,
                notes = "",
                amount = 100.0f
            )
        )

        // delete transactions with this test account
        accountsDao.deleteTransactionsWithAccountId(1)
        accountsDao.deleteAccountDataById(1)

        // assert that there is no transactions
        val transactions = transactionsDao.getTransactionsPerDay()
        assert(transactions.isEmpty())
    }
}