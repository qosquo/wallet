package com.qosquo.wallet.viewmodel

import com.qosquo.wallet.model.database.TransactionsDao
import com.qosquo.wallet.viewmodel.states.CategoriesState
import com.qosquo.wallet.viewmodel.states.TransactionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TransactionViewModel(
    private val dao: TransactionsDao
) {
    private val _state: MutableStateFlow<TransactionState> = MutableStateFlow(
        TransactionState(
            transactionsPerDay = dao.getTransactionsPerDay()
        )
    )
    val state = _state.asStateFlow()

    private var initialState: CategoriesState = CategoriesState()
}