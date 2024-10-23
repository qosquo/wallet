package com.qosquo.wallet.presentation.ui.operations

import com.qosquo.wallet.data.db.dao.TransactionsDao
import com.qosquo.wallet.data.db.entity.TransactionsDbEntity
import com.qosquo.wallet.domain.Colors
import com.qosquo.wallet.domain.Currencies
import com.qosquo.wallet.domain.Icons
import com.qosquo.wallet.utils.signedValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar
import kotlin.math.abs

class TransactionViewModel(
    private val dao: TransactionsDao,
) {
    private val _state: MutableStateFlow<TransactionState> = MutableStateFlow(
        TransactionState(
            transactionsPerDay = dao.getTransactionsPerDay()
        )
    )
    val state = _state.asStateFlow()

    private val _accountState: MutableStateFlow<TransactionAccountState> = MutableStateFlow(
        TransactionAccountState()
    )
    val accountState = _accountState.asStateFlow()

    private val _categoryState: MutableStateFlow<TransactionCategoryState> = MutableStateFlow(
        TransactionCategoryState()
    )
    val categoryState = _categoryState.asStateFlow()

    private var initialState: TransactionState = TransactionState()

    fun onAction(action: OperationsAction) {
        when (action) {
            OperationsAction.SaveTransaction -> {
                // set transaction values from state
                val id = _state.value.id
                val amount = _state.value.amount.toFloatOrNull()?.div(100) ?: 0F
                val accountId = _state.value.accountId
                val categoryId = _state.value.categoryId
                val date = _state.value.date
                val notes = _state.value.notes

                // check if values are correct
                // if not return from function (TODO: THROW EXCEPTION)
                if (abs(amount) < 0.001f || accountId < 0L || categoryId < 0L) {
                    return
                }

                if (initialState.accountId < 0 || initialState.accountId == accountId) {
                    var accountBalance = dao.getAccountBalance(accountId)
                    val initialAmount = initialState.amount.toFloatOrNull()?.div(100) ?: 0F
                    val categoryType = dao.getCategoryTypeId(categoryId)
                    val signedInitialAmount = signedValue(categoryType, initialAmount)
                    val signedAmount = signedValue(categoryType, amount)
                    accountBalance -= signedInitialAmount
                    accountBalance += signedAmount
                    dao.updateAccountBalance(accountBalance, accountId)
                } else {
                    val categoryType = dao.getCategoryTypeId(categoryId)
                    var fromAccountBalance = dao.getAccountBalance(initialState.accountId)
                    var toAccountBalance = dao.getAccountBalance(accountId)
                    val amount = this.initialState.amount.toFloatOrNull()?.div(100)
                    amount?.let {
                        val signedAmount = signedValue(categoryType, it)
                        fromAccountBalance -= signedAmount
                        toAccountBalance += signedAmount
                        dao.updateAccountBalance(fromAccountBalance, initialState.accountId)
                        dao.updateAccountBalance(toAccountBalance, accountId)
                    }
               }

                val newTransaction = TransactionsDbEntity(
                    id = id,
                    amount = amount,
                    accountId = accountId,
                    categoryId = categoryId,
                    date = date,
                    notes = notes
                )

                dao.upsertNewTransaction(newTransaction)
                val updatedTransactions = dao.getTransactionsPerDay()

                _state.update {
                    it.copy(
                        transactionsPerDay = updatedTransactions,
                        id = 0,
                        amount = "",
                        accountId = -1,
                        categoryId = -1,
                        date = -1,
                        notes = ""
                    )
                }
            }
            is OperationsAction.SetAccountId -> {
                if (action.newAccountId != null) {
                    _state.update { it.copy(accountId = action.newAccountId) }
                    val account = dao.getAccountFromId(action.newAccountId)
                    _accountState.update {
                        it.copy(
                            id = account.id,
                            name = account.name,
                            currency = Currencies.entries[account.currency],
                            colorHex = account.colorHex,
                            iconId = account.iconId
                        )
                    }
                } else {
                    _accountState.update {
                        it.copy(
                            id = 0,
                            name = "",
                            currency = Currencies.RUBEL,
                            colorHex = Colors.GRAY.hex,
                            iconId = Icons.Categories.UNKNOWN.id
                        )
                    }
                }
            }
            is OperationsAction.SetAmount -> {
                _state.update { it.copy(amount = action.newAmount) }
            }
            is OperationsAction.SetCategoryId -> {
                if (action.newCategoryId != null) {
                    _state.update { it.copy(categoryId = action.newCategoryId) }
                    val category = dao.getCategoryFromId(action.newCategoryId)
                    _categoryState.update {
                        it.copy(
                            id = category.id,
                            name = category.name,
                            type = category.type,
                            colorHex = category.colorHex,
                            iconId = category.iconId
                        )
                    }
                } else {
                    _categoryState.update {
                        it.copy(
                            id = 0,
                            name = "",
                            type = 0,
                            colorHex = Colors.GRAY.hex,
                            iconId = Icons.Categories.UNKNOWN.id
                        )
                    }
                }
            }
            is OperationsAction.SetDate -> {
                _state.update { it.copy(date = action.newDate) }
            }
            is OperationsAction.SetNotes -> {
                _state.update { it.copy(notes = action.newNotes) }
            }
            is OperationsAction.SetTransactionById -> {
                if (action.transactionId != null) {
                    val transaction = dao.getTransactionFromId(action.transactionId)
                    this.initialState = TransactionState(
                        id = transaction.id,
                        transactionsPerDay = dao.getTransactionsPerDay(),
                        amount = (transaction.amount * 100).toInt().toString(),
                        accountId = transaction.accountId,
                        categoryId = transaction.categoryId,
                        date = transaction.date,
                        notes = transaction.notes
                    )
                } else {
                    if (_state.value.accountId > -1
                        || _state.value.categoryId > -1
                        || _state.value.date > -1) {
                        return
                    }
                    // set current date to exact midnight in its local time zone
                    val calendar = Calendar.getInstance()
                    val millis = calendar.timeInMillis
                    val seconds = (millis / 1000).toInt()
                    val mod = seconds % 86400
                    val midnight = seconds - mod
                    val timezone = calendar.timeZone
                    val offset = timezone.getOffset((midnight * 1000).toLong())
                    val date = midnight.toLong() * 1000 + offset
                    this.initialState = TransactionState(
                        transactionsPerDay = dao.getTransactionsPerDay(),
                        date = date.toLong()
                    )
                }

                _state.value = this.initialState
            }

            OperationsAction.UpdateTransactions -> {
                _state.update {
                    it.copy(transactionsPerDay = dao.getTransactionsPerDay())
                }
            }
        }
    }
}