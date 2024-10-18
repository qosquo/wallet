package com.qosquo.wallet.presentation.ui.accounts


sealed interface AccountsAction {
    data object SaveAccount: AccountsAction

    data class SetAccountById(val accountId: Long?) : AccountsAction
    data class SetName(val newName: String): AccountsAction
    data class SetInitialBalance(val newBalance: String): AccountsAction
    data class SetMustBeCounted(val newValue: Boolean): AccountsAction
    data class SetIconId(val newId: Int) : AccountsAction
    data class SetColorHex(val newHex: String): AccountsAction
    data class SetCurrency(val newCurrency: Int) : AccountsAction
}