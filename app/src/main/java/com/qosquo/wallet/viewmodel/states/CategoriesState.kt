package com.qosquo.wallet.viewmodel.states

import com.qosquo.wallet.model.Category
import com.qosquo.wallet.model.CategoryTypes
import com.qosquo.wallet.ui.Colors
import com.qosquo.wallet.ui.Icons

data class CategoriesState(
    val id: Long = 0,
    val expensesCategories: List<Category> = emptyList(),
    val incomeCategories: List<Category> = emptyList(),
    val name: String = "",
    val type: Short = CategoryTypes.EXPENSES.id,
    val goal: String = "",
    val iconId: Int = Icons.Categories.entries[0].id,
    val colorHex: String = Colors.entries[0].hex
)