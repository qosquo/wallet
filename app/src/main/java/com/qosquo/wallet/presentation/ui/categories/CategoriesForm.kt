package com.qosquo.wallet.presentation.ui.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qosquo.wallet.utils.CurrencyAmountInputVisualTransformation
import com.qosquo.wallet.Dependencies
import com.qosquo.wallet.R
import com.qosquo.wallet.domain.Currencies
import com.qosquo.wallet.domain.Colors
import com.qosquo.wallet.domain.Icons
import com.qosquo.wallet.presentation.ui.accounts.SelectableColor
import com.qosquo.wallet.presentation.ui.common.components.CommonAlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesForm(
    categoryId: Long?,
    type: Int,
    onEvent: (CategoriesAction) -> Unit,
    onNavigate: () -> Unit
) {

    val state by Dependencies.categoriesViewModel.state.collectAsStateWithLifecycle()
    onEvent(CategoriesAction.SetCategoryById(categoryId))
    onEvent(CategoriesAction.SetType(type))

    val openDeleteAlertDialog = remember { mutableStateOf(false) }
    val openExitAlertDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val text = if (type == 0) {
                        stringResource(R.string.categories_form_expense)
                    } else {
                        stringResource(R.string.categories_form_income)
                    }
                    Text(text)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigate) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back button"
                        )
                    }
                },
                actions = {
                    categoryId?.let {
                        IconButton(onClick = {
                            openDeleteAlertDialog.value = true
                        }) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons
                                    .Default.Delete,
                                contentDescription = "delete category"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(CategoriesAction.SaveCategory)
                onNavigate()
            }) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Done,
                    contentDescription = "add"
                )
            }
        },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(NavigationBarDefaults.windowInsets)
    ) { innerPadding ->
        when {
            openDeleteAlertDialog.value -> {
                val systemCategory = if (state.type == 0) {
                    Dependencies.categoriesViewModel.systemExpenseCategory
                } else {
                    Dependencies.categoriesViewModel.systemIncomeCategory
                }
                CommonAlertDialog(
                    onDismissRequest = {
                        openDeleteAlertDialog.value = false
                    },
                    onConfirmation = {
                        openDeleteAlertDialog.value = false
                        onEvent(CategoriesAction.DeleteCategoryById(categoryId!!))
                        onNavigate()
                    },
                    dialogTitle = "Do you really want to delete ${state.name} category?",
                    dialogText = "All operations with this category will move to ${systemCategory.name}"
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Name",
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedTextField(
                value = state.name,
                onValueChange = {
                    onEvent(CategoriesAction.SetName(it))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Text(
                text = "Goal",
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedTextField(
                value = state.goal,
                onValueChange = {
                    val value = if (it.startsWith("0")) {
                        ""
                    } else {
                        it
                    }
                    onEvent(CategoriesAction.SetGoal(value))
                },
                visualTransformation = CurrencyAmountInputVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.NumberPassword
                ),
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            /* TODO: GET DEFAULT CURRENCY FROM SETTINGS */
                            id = Currencies.RUBEL.id
                        ),
                        contentDescription = "Currency icon"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Text(
                text = "Icon",
                style = MaterialTheme.typography.titleMedium,
            )
            LazyVerticalGrid(
                columns = GridCells.Adaptive(64.dp),
            ) {
                val icons = Icons.Categories.entries
                items(icons) { icon ->
                    IconButton(
                        onClick = {
                            onEvent(CategoriesAction.SetIconId(icon.id))
                        },
                    ) {
                        val isSelected = state.iconId == icon.id
                        Icon(
                            imageVector = ImageVector.vectorResource(id = icon.id),
                            contentDescription = "Category icon",
                            tint = if (isSelected) {
                                Color(state.colorHex.toColorInt())
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Text(
                text = "Colors",
                style = MaterialTheme.typography.titleMedium
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                val colors = Colors.entries
                items(colors) { color ->
                    SelectableColor(
                        onClick = {
                            onEvent(CategoriesAction.SetColorHex(color.hex))
                        },
                        modifier = Modifier
                            .size(48.dp),
                        isSelected = color.hex == state.colorHex,
                        colorHex = color.hex
                    )
                }
            }
        }
    }
}