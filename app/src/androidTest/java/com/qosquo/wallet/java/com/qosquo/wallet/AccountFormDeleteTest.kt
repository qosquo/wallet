package com.qosquo.wallet.java.com.qosquo.wallet

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.qosquo.wallet.data.db.dao.AccountsDao
import com.qosquo.wallet.data.db.dao.CategoriesDao
import com.qosquo.wallet.data.db.dao.TransactionsDao
import com.qosquo.wallet.data.db.database.AppDatabase
import com.qosquo.wallet.data.db.entity.AccountsDbEntity
import com.qosquo.wallet.data.db.entity.CategoriesDbEntity
import com.qosquo.wallet.data.db.entity.TransactionsDbEntity
import com.qosquo.wallet.presentation.ui.accounts.AccountsForm
import com.qosquo.wallet.presentation.ui.accounts.AccountsViewModel
import com.qosquo.wallet.presentation.ui.theme.WalletTheme
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class AccountFormDeleteTest {
    @get:Rule val composeTestRule = createComposeRule()

    private lateinit var accountsDao: AccountsDao
    private lateinit var categoriesDao: CategoriesDao
    private lateinit var transactionsDao: TransactionsDao
    private lateinit var accountsViewModel: AccountsViewModel
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

        accountsViewModel = AccountsViewModel(db.getAccountsDao())

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
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun actionButtonDeleteAccountTest() {
        composeTestRule.setContent {
            WalletTheme {
                AccountsForm(
                    accountId = 1,
                    viewModel = accountsViewModel,
                    onEvent = accountsViewModel::onAction,
                    onNavigate = {

                    }
                )
            }
        }
        composeTestRule.onNodeWithContentDescription("delete account").performClick()
        composeTestRule.onNodeWithText("Confirm").assertIsDisplayed().performClick()

        assert(accountsDao.getAllAccountsData().isEmpty())
        assert(transactionsDao.getTransactionsPerDay().isEmpty())
    }
}