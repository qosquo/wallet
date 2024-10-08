package com.qosquo.wallet

import com.qosquo.wallet.model.Category
import com.qosquo.wallet.model.CategoryTypes

sealed interface Event {

    sealed class AccountsEvent: Event {
        data object SaveAccount: AccountsEvent()

        data class SetAccountById(val accountId: Long?) : AccountsEvent()
        data class SetName(val newName: String): AccountsEvent()
        data class SetInitialBalance(val newBalance: Float): AccountsEvent()
        data class SetMustBeCounted(val newValue: Boolean): AccountsEvent()
        data class SetIconId(val newId: Int) : AccountsEvent()
        data class SetColorHex(val newHex: String): AccountsEvent()
    }

    sealed class CategoriesEvent: Event {
        data object SaveCategory: CategoriesEvent()

        data class ShowForm(val category: Category?) : CategoriesEvent()
        data class SetName(val newName: String): CategoriesEvent()
        data class SetType(val newType: CategoryTypes): CategoriesEvent()
        data class SetGoal(val newGoal: Float): CategoriesEvent()
        data class SetIconId(val newId: Int) : CategoriesEvent()
        data class SetColorHex(val newHex: String): CategoriesEvent()
    }

    sealed class TransactionsEvent: Event {
        data object SaveTransaction: TransactionsEvent()
    }

}