package com.qosquo.wallet

import com.qosquo.wallet.model.Account

sealed interface Event {

    sealed class AccountsEvent: Event {
        data object SaveAccount: AccountsEvent()
        data object CloseForm: AccountsEvent()

        data class ShowForm(val account: Account?) : AccountsEvent()
//        data class EditAccount(val account: Account): AccountsEvent()
        data class SetName(val newName: String): AccountsEvent()
        data class SetInitialBalance(val newBalance: Float): AccountsEvent()
        data class SetMustBeCounted(val newValue: Boolean): AccountsEvent()
        data class SetIconId(val newId: Int) : AccountsEvent()
        data class SetColorHex(val newHex: String): AccountsEvent()
    }

    sealed class CategoriesEvent: Event {
        data object SaveCategory: CategoriesEvent()
    }

    sealed class TransactionsEvent: Event {
        data object SaveTransaction: TransactionsEvent()
    }

}