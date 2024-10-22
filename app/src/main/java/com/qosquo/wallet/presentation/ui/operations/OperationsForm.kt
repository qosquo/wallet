@file:OptIn(ExperimentalMaterial3Api::class)

package com.qosquo.wallet.ui.screens.operations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qosquo.wallet.Dependencies
import com.qosquo.wallet.R
import com.qosquo.wallet.presentation.ui.operations.OperationsAction
import com.qosquo.wallet.utils.CurrencyAmountInputVisualTransformation
import com.qosquo.wallet.utils.PastOrPresentSelectableDates
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class OperationsNavigateCode {
    BACK,
    CATEGORY,
    ACCOUNT
}

@Composable
fun OperationsForm(
    transactionId: Long?,
    accountId: Long?,
    categoryId: Long?,
    onAction: (OperationsAction) -> Unit,
    navigate: (code: OperationsNavigateCode) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by Dependencies.transactionsViewModel.state.collectAsStateWithLifecycle()
    val accountState by Dependencies.transactionsViewModel.accountState.collectAsStateWithLifecycle()
    val categoryState by Dependencies.transactionsViewModel.categoryState.collectAsStateWithLifecycle()
    onAction(OperationsAction.SetTransactionById(transactionId))
    onAction(OperationsAction.SetAccountId(accountId))
    onAction(OperationsAction.SetCategoryId(categoryId))

    val calendar = Calendar.getInstance()
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.operations_form))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigate(OperationsNavigateCode.BACK)
                    }) {
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
                onAction(OperationsAction.SaveTransaction)
                navigate(OperationsNavigateCode.BACK)
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
            showDatePicker -> {
                DatePickerModal(
                    initialSelectedDateMillis = state.date,
                    onDateSelected = { unixtime ->
                        unixtime?.let {
                            onAction(OperationsAction.SetDate(it))
                        }
                        showDatePicker = false
                    },
                    onDismiss = {
                        showDatePicker = false
                    }
                )
            }
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Amount",
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                val sign = if (categoryState.type == 0) {
                    "-"
                } else if (categoryState.type == 1){
                    "+"
                } else {
                    "?"
                    // TODO: ADD EXCEPTION
                }
                Text(
                    text = sign,
                    style = MaterialTheme.typography.displaySmall,
                    modifier = modifier
                        .padding(16.dp)
                )
                OutlinedTextField(
                    value = state.amount,
                    onValueChange = {
                        val value = if (it.startsWith("0")) {
                            ""
                        } else {
                            it
                        }
                        onAction(OperationsAction.SetAmount(value))
                    },
                    visualTransformation = CurrencyAmountInputVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                id = accountState.currency.id
                            ),
                            contentDescription = "Currency icon"
                        )
                    },
                    modifier = modifier
                        .weight(1f)
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Text(
                text = "Account",
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedCard(
                onClick = {
                    navigate(OperationsNavigateCode.ACCOUNT)
                },
                modifier = modifier
                    .fillMaxWidth()
            ) {
                ListItem(
                    headlineContent = {
                        Text(text = accountState.name)
                    },
                    leadingContent = {
                        val color = accountState.colorHex
                        val iconId = accountState.iconId
                        Box(
                            modifier = modifier
                                .size(36.dp)
                                .wrapContentSize(align = Alignment.Center)
                                .clip(CircleShape)
                                .background(Color(color.toColorInt()))
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    id = iconId
                                ),
                                contentDescription = "bus",
                                tint = Color.White,
                                modifier = modifier
                                    .fillMaxSize()
                                    .padding(4.dp)
                            )
                        }
                    },
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Text(
                text = "Category",
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedCard(
                onClick = {
                    navigate(OperationsNavigateCode.CATEGORY)
                },
                modifier = modifier
                    .fillMaxWidth()
            ) {
                ListItem(
                    headlineContent = {
                        Text(text = categoryState.name)
                    },
                    leadingContent = {
                        val color = categoryState.colorHex
                        val iconId = categoryState.iconId
                        Box(
                            modifier = modifier
                                .size(36.dp)
                                .wrapContentSize(align = Alignment.Center)
                                .clip(CircleShape)
                                .background(Color(color.toColorInt()))
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    id = iconId
                                ),
                                contentDescription = "bus",
                                tint = Color.White,
                                modifier = modifier
                                    .fillMaxSize()
                                    .padding(4.dp)
                            )
                        }
                    },
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Text(
                text = "Date",
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    showDatePicker = true
                }
            ) {
                ListItem(
                    headlineContent = {
                        Text(
                            text = convertMillisToDate(state.date)
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.DateRange,
                            contentDescription = "date"
                        )
                    },
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Text(
                text = "Notes",
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedTextField(
                value = state.notes,
                onValueChange = {
                    onAction(OperationsAction.SetNotes(it))
                },
                placeholder = {
                    Text(text = "Add a note")
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun DatePickerModal(
    initialSelectedDateMillis: Long?,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis,
        selectableDates = PastOrPresentSelectableDates
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat(/* pattern = */ "MM/dd/yyyy", /* locale = */ Locale.getDefault())
    return formatter.format(Date(millis))
}