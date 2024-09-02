package com.qosquo.wallet.viewmodel.states

import com.qosquo.wallet.model.Account

data class AccountsState(
    val accounts: List<Account> = emptyList(),
    val name: String = "",
    val initialBalance: Float = 0F,
    val mustBeCounted: Boolean = true,
    val iconId: Int = 0,
    val colorHex: String = "#000000",
)
