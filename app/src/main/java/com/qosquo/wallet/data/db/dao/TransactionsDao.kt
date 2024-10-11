package com.qosquo.wallet.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.qosquo.wallet.model.Transaction
import com.qosquo.wallet.model.database.entities.TransactionsDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao {

    @Insert(entity = TransactionsDbEntity::class)
    suspend fun insertNewTransactionData(transaction: TransactionsDbEntity)

    @Query("SELECT transactions.id, transactions.amount, account_name, category_name, " +
            "category_icon_id, categories.color_hex, " +
            "transactions.date, transactions.notes FROM transactions " +
            "INNER JOIN accounts ON transactions.account_id = accounts.id " +
            "INNER JOIN categories ON transactions.category_id = categories.id; ")
    fun getAllTransactionsData(): List<Transaction>

    @Query("DELETE FROM transactions WHERE ID = :transactionId")
    suspend fun deleteTransactionDataById(transactionId: Long)

}