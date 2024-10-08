package com.qosquo.wallet.ui.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qosquo.wallet.Dependencies
import com.qosquo.wallet.Event
import com.qosquo.wallet.ui.Colors
import com.qosquo.wallet.ui.Icons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsForm(
    accountId: Long?,
    onEvent: (Event.AccountsEvent) -> Unit,
    onNavigate: () -> Unit
) {
    val state by Dependencies.accountsViewModel.state.collectAsStateWithLifecycle()
    onEvent(Event.AccountsEvent.SetAccountById(accountId))
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(Event.AccountsEvent.SaveAccount)
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
            Row(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.5f)
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextField(
                    value = state.initialBalance.toString(),
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp
                    ),
                    onValueChange = {
                        val value = it.toFloatOrNull()
                        if (value != null) {
                            onEvent(Event.AccountsEvent.SetInitialBalance(it.toFloat()))
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.weight(.5f)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = "RUB", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "Account name")
            TextField(
                value = state.name,
                onValueChange = {
                    onEvent(Event.AccountsEvent.SetName(it))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "Icons")
            val size = 64.dp
            LazyVerticalGrid(
                columns = GridCells.Adaptive(size),
            ) {
                val icons = Icons.Accounts.entries
                items(icons) { icon ->
                    SelectableCard(
                        isSelected = state.iconId == icon.id,
                        modifier = Modifier
                            .size(size)
                            .clickable {
                                onEvent(Event.AccountsEvent.SetIconId(icon.id))
                            },
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                id = icon.id
                            ),
                            contentDescription = "Account icon",
                            tint = Color.Black,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(Color("#cccccc".toColorInt()))
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "Colors")
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                val colors = Colors.entries
                items(colors) { color ->
                    SelectableColor(
                        modifier = Modifier
                            .size(48.dp)
                            .clickable {
                                onEvent(Event.AccountsEvent.SetColorHex(color.hex))
                            },
                        isSelected = color.hex == state.colorHex,
                        colorHex = color.hex
                    )
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Must be counted in overall balance",
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = state.mustBeCounted,
                    onCheckedChange = {
                        onEvent(Event.AccountsEvent.SetMustBeCounted(it))
                    }
                )
            }
        }
    }
}

@Composable
private fun SelectableCard(
    isSelected: Boolean = false,
    modifier: Modifier,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.background
            }
        ),
        modifier = modifier
            .wrapContentSize(align = Alignment.Center),
        content = content
    )
}

@Composable
private fun SelectableColor(
    isSelected: Boolean = false,
    colorHex: String = "#000000",
    modifier: Modifier,
) {
    Box(modifier = modifier
        .wrapContentSize(align = Alignment.Center)
        .padding(4.dp)
        .clip(CircleShape)
        .background(Color(colorHex.toColorInt()))) {
        val tint: Color = if (isSelected) Color.White else Color.Transparent

        Icon(
            imageVector = androidx.compose.material.icons.Icons.Default.Done,
            contentDescription = "color selected",
            tint = tint,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        )
    }
}