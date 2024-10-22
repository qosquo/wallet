package com.qosquo.wallet.presentation.ui.categories

import com.qosquo.wallet.domain.Category
import com.qosquo.wallet.domain.Colors
import com.qosquo.wallet.domain.Icons

data class CategoriesState(
    val id: Long = 0,
    val expensesCategories: List<Category> = emptyList(),
    val incomeCategories: List<Category> = emptyList(),
    val name: String = "",
    val type: Int = 0,
    val goal: String = "",
    val iconId: Int = Icons.Categories.entries[0].id,
    val colorHex: String = Colors.entries[0].hex
)