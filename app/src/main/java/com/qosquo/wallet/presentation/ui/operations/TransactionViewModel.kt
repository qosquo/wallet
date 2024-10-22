package com.qosquo.wallet.presentation.ui.operations

import com.qosquo.wallet.data.db.dao.TransactionsDao
import com.qosquo.wallet.data.db.entity.TransactionsDbEntity
import com.qosquo.wallet.domain.CategoryTypes
import com.qosquo.wallet.domain.Colors
import com.qosquo.wallet.domain.Currencies
import com.qosquo.wallet.domain.Icons
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
                val id = _state.value.id
                val amount = _state.value.amount.toFloatOrNull() ?: 0F
                val accountId = _state.value.accountId
                val categoryId = _state.value.categoryId
                val date = _state.value.date
                val notes = _state.value.notes

                if (abs(amount) < 0.001f || accountId == 0L || categoryId == 0L
                    || date <= 0) {
                    return
                }

                val newTransaction = TransactionsDbEntity(
                    id = id,
                    amount = amount.div(100),
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
                        date = 0,
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
                            type = CategoryTypes.entries[category.typeId],
                            colorHex = category.colorHex,
                            iconId = category.iconId
                        )
                    }
                } else {
                    _categoryState.update {
                        it.copy(
                            id = 0,
                            name = "",
                            type = CategoryTypes.EXPENSES,
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
        }
    }
}