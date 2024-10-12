package com.qosquo.wallet.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.Query
import com.qosquo.wallet.model.Transaction
import com.qosquo.wallet.model.database.entities.TransactionsDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao {

    @Insert(entity = TransactionsDbEntity::class)
    suspend fun insertNewTransactionData(transaction: TransactionsDbEntity)

    @Query("SELECT transactions.id, transactions.amount, account_name, accounts.currency, " +
            "category_name, category_icon_id, categories.color_hex, " +
            "transactions.date, transactions.notes FROM transactions " +
            "INNER JOIN accounts ON transactions.account_id = accounts.id " +
            "INNER JOIN categories ON transactions.category_id = categories.id " +
            "ORDER BY transactions.date DESC, transactions.id DESC; ")
    fun getTransactionsPerDay(): Map<@MapColumn("date") Long, List<Transaction>>

    @Query("DELETE FROM transactions WHERE ID = :transactionId")
    suspend fun deleteTransactionDataById(transactionId: Long)

}