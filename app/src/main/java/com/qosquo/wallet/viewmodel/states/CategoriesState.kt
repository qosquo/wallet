package com.qosquo.wallet.viewmodel.states

import com.qosquo.wallet.model.Category
import com.qosquo.wallet.model.CategoryTypes

data class CategoriesState(
    val categories: List<Category> = emptyList(),
    val name: String = "",
    val type: Short = CategoryTypes.EXPENSES.id,
    val goal: Float = 0f,
    val iconId: Int = 0,
    val colorHex: String = ""
)