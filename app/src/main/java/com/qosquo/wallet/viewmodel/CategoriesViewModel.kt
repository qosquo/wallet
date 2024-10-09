package com.qosquo.wallet.viewmodel

import com.qosquo.wallet.Event
import com.qosquo.wallet.model.CategoryTypes
import com.qosquo.wallet.model.database.CategoriesDao
import com.qosquo.wallet.model.database.entities.CategoriesDbEntity
import com.qosquo.wallet.viewmodel.states.CategoriesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CategoriesViewModel(
    private val dao: CategoriesDao
) {
    private val _state: MutableStateFlow<CategoriesState> = MutableStateFlow(
        CategoriesState(categories = dao.getAllCategoriesData())
    )
    val state = _state.asStateFlow()

    private var initialState: CategoriesState = CategoriesState()
    fun canExitForm(): Boolean {
        return initialState == _state.value
    }

    fun onEvent(event: Event.CategoriesEvent) {
        when (event) {
            Event.CategoriesEvent.SaveCategory -> {
                val name = _state.value.name
                val type = _state.value.type
                val goal = _state.value.goal.toFloatOrNull()
                val iconId = _state.value.iconId
                val colorHex = _state.value.colorHex

                if (name.isBlank() || (iconId == 0) || colorHex.isBlank()
                    || (colorHex == "#000000") || (type > 2) || (type < 0) || (goal == null)
                    || (goal < 0)
                ) {
                    return
                }

                val newCategory = CategoriesDbEntity(
                    id = 0,
                    categoryName = name,
                    type = type,
                    goal = goal / 100,
                    categoryIconId = iconId,
                    colorHex = colorHex
                )

                dao.upsertNewCategoryData(newCategory)
                val updatedCategories = dao.getAllCategoriesData()

                _state.update {
                    it.copy(
                        categories = updatedCategories,
                        name = "",
                        type = CategoryTypes.EXPENSES.id,
                        goal = "",
                        iconId = 0,
                        colorHex = "",
                    )
                }
            }
            is Event.CategoriesEvent.SetColorHex -> {
                _state.update { it.copy(colorHex = event.newHex) }
            }
            is Event.CategoriesEvent.SetGoal -> {
                _state.update { it.copy(goal = event.newGoal) }
            }
            is Event.CategoriesEvent.SetIconId -> {
                _state.update { it.copy(iconId = event.newId) }
            }
            is Event.CategoriesEvent.SetName -> {
                _state.update { it.copy(name = event.newName) }
            }
            is Event.CategoriesEvent.SetType -> {
                _state.update { it.copy(type = event.newType.id) }
            }
            is Event.CategoriesEvent.SetCategoryById -> {
                if (event.categoryId != null) {
                    val category = dao.getCategoryFromId(event.categoryId)
                    this.initialState = CategoriesState(
                        categories = dao.getAllCategoriesData(),
                        name = category.name,
                        type = category.type,
                        goal = (category.goal * 100).toInt().toString(),
                        iconId = category.iconId,
                        colorHex = category.colorHex,
                    )
                } else {
                    this.initialState = CategoriesState(
                        categories = dao.getAllCategoriesData()
                    )
                }

                _state.value = this.initialState
            }
        }
    }
}