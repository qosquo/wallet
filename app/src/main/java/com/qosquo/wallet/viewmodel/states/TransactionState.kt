package com.qosquo.wallet.viewmodel.states

import com.qosquo.wallet.model.Transaction

data class TransactionState(
    val id: Long = 0,
    val transactionsPerDay: Map<Long, List<Transaction>> = emptyMap(),
    val amount: Float = 0F,
    val accountId: Long = -1,
    val categoryId: Long = -1,
    val date: Long = -1,
    val notes: String = ""
)