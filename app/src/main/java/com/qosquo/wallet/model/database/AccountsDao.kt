package com.qosquo.wallet.model.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.qosquo.wallet.model.Account
import com.qosquo.wallet.model.database.entities.AccountsDbEntity

@Dao
interface AccountsDao {

    @Upsert(entity = AccountsDbEntity::class)
    fun upsertNewAccountData(account: AccountsDbEntity)

    @Query("SELECT accounts.id, accounts.balance, accounts.account_name AS name, " +
            "accounts.account_icon_id AS accountIconId, " +
            "accounts.color_hex AS colorHex, accounts.count FROM accounts")
    fun getAllAccountsData(): List<Account>

    @Query("SELECT accounts.id, accounts.balance, accounts.account_name AS name, " +
            "accounts.account_icon_id AS accountIconId, " +
            "accounts.color_hex AS colorHex, accounts.count FROM accounts " +
            "WHERE ID = :accountId LIMIT 1")
    fun getAccountFromId(accountId: Long) : Account

    @Query("DELETE FROM accounts WHERE ID = :accountId")
    fun deleteAccountDataById(accountId: Long)

}