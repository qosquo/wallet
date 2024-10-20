package com.qosquo.wallet.presentation.ui.operations

import androidx.annotation.DrawableRes
import com.qosquo.wallet.domain.CategoryTypes
import com.qosquo.wallet.domain.Colors
import com.qosquo.wallet.domain.Currencies
import com.qosquo.wallet.domain.Icons
import com.qosquo.wallet.domain.Transaction

data class TransactionState(
    val id: Long = 0,
    val transactionsPerDay: Map<Long, List<Transaction>> = emptyMap(),
    val amount: String = "",
    val accountId: Long = -1,
    val categoryId: Long = -1,
    val date: Long = -1,
    val notes: String = ""
)

data class TransactionAccountState(
    val id: Long = 0,
    val name: String = "",
    val currency: Currencies = Currencies.RUBEL,
    val colorHex: String = Colors.GRAY.hex,
    @DrawableRes val iconId: Int = Icons.Categories.UNKNOWN.id
)

data class TransactionCategoryState(
    val id: Long = 0,
    val name: String = "",
    val type: CategoryTypes = CategoryTypes.EXPENSES,
    val colorHex: String = Colors.GRAY.hex,
    @DrawableRes val iconId: Int = Icons.Categories.UNKNOWN.id
)