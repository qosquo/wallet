package com.qosquo.wallet.presentation.ui.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextButton
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qosquo.wallet.utils.CurrencyAmountInputVisualTransformation
import com.qosquo.wallet.Dependencies
import com.qosquo.wallet.R
import com.qosquo.wallet.domain.Currencies
import com.qosquo.wallet.domain.Colors
import com.qosquo.wallet.domain.Icons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsForm(
    accountId: Long?,
    onEvent: (AccountsAction) -> Unit,
    onNavigate: () -> Unit
) {
    val state by Dependencies.accountsViewModel.state.collectAsStateWithLifecycle()
    onEvent(AccountsAction.SetAccountById(accountId))

    val openCurrenciesDialog = remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.accounts_form))
                },
                navigationIcon = {
                    IconButton(onClick = onNavigate) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back button"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(AccountsAction.SaveAccount)
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
            openCurrenciesDialog.value -> {
                CurrenciesDialog(
                    onDismissRequest = {
                        openCurrenciesDialog.value = false
                    },
                    onConfirmation = { value ->
                        onEvent(AccountsAction.SetCurrency(value))
                        openCurrenciesDialog.value = false
                    },
                    defaultValue = state.currency
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
                text = "Account name",
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedTextField(
                value = state.name,
                onValueChange = {
                    onEvent(AccountsAction.SetName(it))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Text(
                text = "Initial balance",
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedTextField(
                value = state.initialBalance,
                onValueChange = {
                    val value = if (it.startsWith("0")) {
                        ""
                    } else {
                        it
                    }
                    onEvent(AccountsAction.SetInitialBalance(value))
                },
                visualTransformation = CurrencyAmountInputVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.NumberPassword
                ),
                leadingIcon = {
                    IconButton(onClick = {
                        openCurrenciesDialog.value = true
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                id = Currencies.entries[state.currency].id
                            ),
                            contentDescription = "Currency icon"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Text(
                text = "Icons",
                style = MaterialTheme.typography.titleMedium,
            )
            LazyVerticalGrid(
                columns = GridCells.Adaptive(64.dp),
            ) {
                val icons = Icons.Accounts.entries
                items(icons) { icon ->
                    IconButton(
                        onClick = {
                            onEvent(AccountsAction.SetIconId(icon.id))
                        },
                    ) {
                        val isSelected = state.iconId == icon.id
                        Icon(
                            imageVector = ImageVector.vectorResource(id = icon.id),
                            contentDescription = "Account icon",
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
                            onEvent(AccountsAction.SetColorHex(color.hex))
                        },
                        modifier = Modifier
                            .size(48.dp),
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
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium
                )
                Switch(
                    checked = state.mustBeCounted,
                    onCheckedChange = {
                        onEvent(AccountsAction.SetMustBeCounted(it))
                    }
                )
            }
        }
    }
}

@Composable
fun CurrenciesDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (newValue: Int) -> Unit,
    defaultValue: Int
) {
    val selectedValue = remember {
        mutableIntStateOf(defaultValue)
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select account currency",
                    style = MaterialTheme.typography.titleLarge
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val currencies = Currencies.entries
                    items(currencies) { currency ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = currency.ordinal == selectedValue.intValue,
                                    onClick = {
                                        selectedValue.intValue = currency.ordinal
                                    }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = currency.ordinal == selectedValue.intValue,
                                onClick = {
                                    selectedValue.intValue = currency.ordinal
                                }
                            )
                            Text(text = currency.name)
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = {
                            onConfirmation(selectedValue.intValue)
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun SelectableColor(
    onClick: () -> Unit,
    isSelected: Boolean = false,
    colorHex: String = "#000000",
    modifier: Modifier,
) {
    IconButton(onClick = onClick) {
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
}