package com.qosquo.wallet.presentation.ui.categories

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.stringResource
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.qosquo.wallet.R
import com.qosquo.wallet.data.db.dao.CategoriesDao
import com.qosquo.wallet.data.db.entity.CategoriesDbEntity
import com.qosquo.wallet.domain.Category
import com.qosquo.wallet.domain.Colors
import com.qosquo.wallet.domain.Icons
import com.qosquo.wallet.utils.PreferencesDataStoreHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

class CategoriesViewModel(
    private val dao: CategoriesDao
) {
    private val _state: MutableStateFlow<CategoriesState> = MutableStateFlow(
        CategoriesState(
            expensesCategories = dao.getCategoriesOfType(0),
            incomeCategories = dao.getCategoriesOfType(1)
        )
    )
    val state = _state.asStateFlow()

    private var initialState: CategoriesState = CategoriesState()
    fun canExitForm(): Boolean {
        return initialState == _state.value
    }

    fun insertSystemCategories(context: Context) {
        val expenseCategoryName = context.resources.getString(R.string.categories_system_expense_name)
        val incomeCategoryName = context.resources.getString(R.string.categories_system_income_name)
        if (dao.getAllCategoriesData().isEmpty()) {
            val expenseCategoryId = dao.upsertNewCategoryData(CategoriesDbEntity(
                id = 0,
                categoryName = expenseCategoryName,
                type = 0,
                goal = 0F,
                categoryIconId = Icons.Categories.UNKNOWN.id,
                colorHex = Colors.RED.hex
            ))
            val incomeCategoryId = dao.upsertNewCategoryData(CategoriesDbEntity(
                id = 0,
                categoryName = incomeCategoryName,
                type = 1,
                goal = 0F,
                categoryIconId = Icons.Categories.UNKNOWN.id,
                colorHex = Colors.GREEN.hex
            ))
            _state.update { it.copy(
                expensesCategories = dao.getCategoriesOfType(0),
                incomeCategories = dao.getCategoriesOfType(1)
            ) }
            runBlocking {
                PreferencesDataStoreHelper.saveLongValue(
                    value = expenseCategoryId,
                    key = longPreferencesKey("expenseCategoryId"),
                    context = context
                )
                PreferencesDataStoreHelper.saveLongValue(
                    value = incomeCategoryId,
                    key = longPreferencesKey("incomeCategoryId"),
                    context = context
                )
                // PreferencesDataStoreHelper.getLongValueFlow(
                //     longPreferencesKey("expenseCategoryId"),
                //     context
                // ).collect {
                //     print(it)
                // }
            }
        }
    }

    val systemExpenseCategory: Category = dao.getSystemExpenseCategory()
    val systemIncomeCategory: Category = dao.getSystemIncomeCategory()

    fun onAction(action: CategoriesAction) {
        when (action) {
            CategoriesAction.SaveCategory -> {
                val id = _state.value.id
                val name = _state.value.name
                val type = _state.value.type
                val goal = _state.value.goal.toFloatOrNull()
                val iconId = _state.value.iconId
                val colorHex = _state.value.colorHex

                if (name.isBlank() || (iconId == 0) || colorHex.isBlank()
                    || (colorHex == "#000000") || (type > 2) || (type < 0)
                ) {
                    return
                }

                val newCategory = CategoriesDbEntity(
                    id = id,
                    categoryName = name,
                    type = type,
                    goal = goal?.div(100) ?: 0F,
                    categoryIconId = iconId,
                    colorHex = colorHex
                )

                dao.upsertNewCategoryData(newCategory)
                val updatedExpenses = dao.getCategoriesOfType(0)
                val updatedIncome = dao.getCategoriesOfType(1)

                _state.update {
                    it.copy(
                        expensesCategories = updatedExpenses,
                        incomeCategories = updatedIncome,
                        name = "",
                        type = 0,
                        goal = "",
                        iconId = 0,
                        colorHex = "",
                    )
                }
            }
            is CategoriesAction.SetColorHex -> {
                _state.update { it.copy(colorHex = action.newHex) }
            }
            is CategoriesAction.SetGoal -> {
                _state.update { it.copy(goal = action.newGoal) }
            }
            is CategoriesAction.SetIconId -> {
                _state.update { it.copy(iconId = action.newId) }
            }
            is CategoriesAction.SetName -> {
                _state.update { it.copy(name = action.newName) }
            }
            is CategoriesAction.SetType -> {
                _state.update { it.copy(type = action.newType) }
            }
            is CategoriesAction.SetCategoryById -> {
                if (action.categoryId != null) {
                    val category = dao.getCategoryFromId(action.categoryId)
                    this.initialState = CategoriesState(
                        expensesCategories = dao.getCategoriesOfType(0),
                        incomeCategories = dao.getCategoriesOfType(1),
                        id = category.id,
                        name = category.name,
                        type = category.type,
                        goal = (category.goal * 100).toInt().toString(),
                        iconId = category.iconId,
                        colorHex = category.colorHex,
                    )
                } else {
                    this.initialState = CategoriesState(
                        expensesCategories = dao.getCategoriesOfType(0),
                        incomeCategories = dao.getCategoriesOfType(1)
                    )
                }

                _state.value = this.initialState
            }

            is CategoriesAction.DeleteCategoryById -> {
                dao.updateTransactionsDeletedCategoryId(_state.value.type, action.categoryId)
                dao.deleteCategoryDataById(action.categoryId)
                _state.update {
                    it.copy(
                        expensesCategories = dao.getCategoriesOfType(0),
                        incomeCategories = dao.getCategoriesOfType(1),
                        name = "",
                        type = 0,
                        goal = "",
                        iconId = 0,
                        colorHex = "",
                    )
                }
            }
        }
    }
}