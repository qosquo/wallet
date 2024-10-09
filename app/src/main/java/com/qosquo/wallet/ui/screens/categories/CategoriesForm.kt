package com.qosquo.wallet.ui.screens.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qosquo.wallet.CurrencyAmountInputVisualTransformation
import com.qosquo.wallet.Dependencies
import com.qosquo.wallet.Event
import com.qosquo.wallet.model.Currencies
import com.qosquo.wallet.ui.Colors
import com.qosquo.wallet.ui.Icons
import com.qosquo.wallet.ui.screens.accounts.CurrenciesDialog
import com.qosquo.wallet.ui.screens.accounts.SelectableColor

@Composable
fun CategoriesForm(
    categoryId: Long?,
    onEvent: (Event.CategoriesEvent) -> Unit,
    onNavigate: () -> Unit
) {

    val state by Dependencies.categoriesViewModel.state.collectAsStateWithLifecycle()
    onEvent(Event.CategoriesEvent.SetCategoryById(categoryId))

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(Event.CategoriesEvent.SaveCategory)
                onNavigate()
            }) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Done,
                    contentDescription = "add"
                )
            }
        }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Name",
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedTextField(
                value = state.name,
                onValueChange = {
                    onEvent(Event.CategoriesEvent.SetName(it))
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
                    onEvent(Event.CategoriesEvent.SetGoal(value))
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
                            onEvent(Event.CategoriesEvent.SetIconId(icon.id))
                        },
                    ) {
                        val isSelected = state.iconId == icon.id
                        Icon(
                            imageVector = ImageVector.vectorResource(id = icon.id),
                            contentDescription = "Category icon",
                            tint = if (isSelected) {
                                Color(state.colorHex.toColorInt())
                            } else {
                                Color.Black
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
                            onEvent(Event.CategoriesEvent.SetColorHex(color.hex))
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