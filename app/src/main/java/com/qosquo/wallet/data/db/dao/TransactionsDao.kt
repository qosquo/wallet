package com.qosquo.wallet.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.Query
import androidx.room.Upsert
import com.qosquo.wallet.data.db.entity.TransactionsDbEntity
import com.qosquo.wallet.domain.Transaction
import com.qosquo.wallet.domain.TransactionAccount
import com.qosquo.wallet.domain.TransactionCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao {

    @Upsert(entity = TransactionsDbEntity::class)
    fun upsertNewTransaction(transaction: TransactionsDbEntity)

    @Query("SELECT transactions.id, transactions.amount, " +
            "accounts.id AS account_id, account_name, account_icon_id, " +
            "account_icon_id, accounts.color_hex AS account_color_hex, " +
            "accounts.currency AS account_currency, " +
            "categories.id AS category_id, category_name, " +
            "categories.type AS category_type_id, category_icon_id, " +
            "categories.color_hex AS category_color_hex, " +
            "transactions.date, transactions.notes FROM transactions " +
            "INNER JOIN accounts ON transactions.account_id = accounts.id " +
            "INNER JOIN categories ON transactions.category_id = categories.id " +
            "WHERE transactions.id = :transactionId LIMIT 1; ")
    fun getTransactionFromId(transactionId: Long) : Transaction

    @Query("""
        SELECT accounts.id, accounts.account_name AS name, 
        accounts.currency, accounts.color_hex AS color_hex,
        accounts.account_icon_id AS icon_id FROM accounts
        WHERE accounts.id = :accountId LIMIT 1
    """)
    fun getAccountFromId(accountId: Long) : TransactionAccount

    @Query("""
        SELECT categories.id, categories.category_name AS name,
        categories.type AS type_id, categories.color_hex, categories.category_icon_id AS icon_id
        FROM categories 
        WHERE categories.id = :categoryId LIMIT 1
    """)
    fun getCategoryFromId(categoryId: Long) : TransactionCategory

    @Query("SELECT transactions.id, transactions.amount, " +
            "accounts.id AS account_id, account_name, account_icon_id, " +
            "account_icon_id, accounts.color_hex AS account_color_hex, " +
            "accounts.currency AS account_currency, " +
            "categories.id AS category_id, category_name, " +
            "categories.type AS category_type_id, category_icon_id, " +
            "categories.color_hex AS category_color_hex, " +
            "transactions.date, transactions.notes FROM transactions " +
            "INNER JOIN accounts ON transactions.account_id = accounts.id " +
            "INNER JOIN categories ON transactions.category_id = categories.id " +
            "ORDER BY transactions.date DESC, transactions.id DESC; ")
    fun getTransactionsPerDay(): Map<@MapColumn("date") Long, List<Transaction>>

    @Query("DELETE FROM transactions WHERE ID = :transactionId")
    suspend fun deleteTransactionDataById(transactionId: Long)

}