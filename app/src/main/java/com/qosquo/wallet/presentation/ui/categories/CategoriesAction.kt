package com.qosquo.wallet.presentation.ui.categories

sealed interface CategoriesAction {
    data object SaveCategory: CategoriesAction

    data class SetCategoryById(val categoryId: Long?) : CategoriesAction
    data class SetName(val newName: String): CategoriesAction
    data class SetType(val newType: Int): CategoriesAction
    data class SetGoal(val newGoal: String): CategoriesAction
    data class SetIconId(val newId: Int) : CategoriesAction
    data class SetColorHex(val newHex: String): CategoriesAction
}