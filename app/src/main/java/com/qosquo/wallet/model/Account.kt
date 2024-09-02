package com.qosquo.wallet.model

import androidx.annotation.DrawableRes
import com.qosquo.wallet.model.database.entities.AccountsDbEntity

data class Account(
    val id: Long,
    val balance: Float,
    val name: String,
    @DrawableRes val accountIconId: Int,
    val colorHex: String,
    val count: Boolean
) {
    fun toAccountsDbEntity(): AccountsDbEntity = AccountsDbEntity(
        id = id,
        balance = this.balance,
        accountName = this.name,
        accountIconId = this.accountIconId,
        colorHex = this.colorHex,
        count = this.count
    )
}
