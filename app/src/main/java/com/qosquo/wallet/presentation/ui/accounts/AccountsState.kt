package com.qosquo.wallet.presentation.ui.accounts

import com.qosquo.wallet.domain.Account
import com.qosquo.wallet.domain.Currencies
import com.qosquo.wallet.domain.Colors
import com.qosquo.wallet.domain.Icons

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
