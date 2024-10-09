package com.qosquo.wallet.viewmodel.states

import com.qosquo.wallet.model.Account
import com.qosquo.wallet.model.Currencies
import com.qosquo.wallet.ui.Colors
import com.qosquo.wallet.ui.Icons

data class AccountsState(
    val accounts: List<Account> = emptyList(),
    val id: Long = 0,
    val name: String = "",
    val initialBalance: String = "",
    val currency: Int = Currencies.RUBEL.ordinal,
    val mustBeCounted: Boolean = true,
    val iconId: Int = Icons.Accounts.entries[0].id,
    val colorHex: String = Colors.entries[0].hex,
)
