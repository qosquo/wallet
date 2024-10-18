package com.qosquo.wallet.presentation.ui.categories

import com.qosquo.wallet.domain.CategoryTypes

sealed interface CategoriesAction {
    data object SaveCategory: CategoriesAction

    data class SetCategoryById(val categoryId: Long?) : CategoriesAction
    data class SetName(val newName: String): CategoriesAction
    data class SetType(val newType: CategoryTypes): CategoriesAction
    data class SetGoal(val newGoal: String): CategoriesAction
    data class SetIconId(val newId: Int) : CategoriesAction
    data class SetColorHex(val newHex: String): CategoriesAction
}