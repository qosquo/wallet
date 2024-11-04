package com.qosquo.wallet.presentation.ui.operations

sealed interface OperationsAction {
    data object SaveTransaction : OperationsAction
    data object UpdateTransactions: OperationsAction

    data class SetAccountId(val newAccountId: Long?) : OperationsAction
    data class SetCategoryId(val newCategoryId: Long?) : OperationsAction
    data class SetAmount(val newAmount: String) : OperationsAction
    data class SetDate(val newDate: Long) : OperationsAction
    data class SetNotes(val newNotes: String) : OperationsAction
    data class SetTransactionById(val transactionId: Long?) : OperationsAction

    data class DeleteTransaction(val transactionId: Long) : OperationsAction
}