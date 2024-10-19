package com.qosquo.wallet.viewmodel

import com.qosquo.wallet.Event
import com.qosquo.wallet.model.CategoryTypes
import com.qosquo.wallet.model.Currencies
import com.qosquo.wallet.model.database.TransactionsDao
import com.qosquo.wallet.model.database.entities.TransactionsDbEntity
import com.qosquo.wallet.ui.Colors
import com.qosquo.wallet.ui.Icons
import com.qosquo.wallet.viewmodel.states.CategoriesState
import com.qosquo.wallet.viewmodel.states.TransactionAccountState
import com.qosquo.wallet.viewmodel.states.TransactionCategoryState
import com.qosquo.wallet.viewmodel.states.TransactionState
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

    fun onEvent(event: Event.TransactionsEvent) {
        when (event) {
            Event.TransactionsEvent.SaveTransaction -> {
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
            is Event.TransactionsEvent.SetAccountId -> {
                if (event.newAccountId != null) {
                    _state.update { it.copy(accountId = event.newAccountId) }
                    val account = dao.getAccountFromId(event.newAccountId)
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
            is Event.TransactionsEvent.SetAmount -> {
                _state.update { it.copy(amount = event.newAmount) }
            }
            is Event.TransactionsEvent.SetCategoryId -> {
                if (event.newCategoryId != null) {
                    _state.update { it.copy(categoryId = event.newCategoryId) }
                    val category = dao.getCategoryFromId(event.newCategoryId)
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
            is Event.TransactionsEvent.SetDate -> {
                _state.update { it.copy(date = event.newDate) }
            }
            is Event.TransactionsEvent.SetNotes -> {
                _state.update { it.copy(notes = event.newNotes) }
            }
            is Event.TransactionsEvent.SetTransactionById -> {
                if (event.transactionId != null) {
                    val transaction = dao.getTransactionFromId(event.transactionId)
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
                    this.initialState = TransactionState(
                        transactionsPerDay = dao.getTransactionsPerDay(),
                        date = Calendar.getInstance().timeInMillis
                    )
                }

                _state.value = this.initialState
            }
        }
    }
}