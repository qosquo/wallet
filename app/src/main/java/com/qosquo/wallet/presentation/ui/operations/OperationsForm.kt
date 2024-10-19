@file:OptIn(ExperimentalMaterial3Api::class)

package com.qosquo.wallet.ui.screens.operations

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qosquo.wallet.CurrencyAmountInputVisualTransformation
import com.qosquo.wallet.Dependencies
import com.qosquo.wallet.Event
import com.qosquo.wallet.model.CategoryTypes
import com.qosquo.wallet.model.Currencies
import com.qosquo.wallet.model.Transaction
import com.qosquo.wallet.ui.Colors
import com.qosquo.wallet.ui.Icons
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs

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
    onEvent: (Event.TransactionsEvent) -> Unit,
    navigate: (code: OperationsNavigateCode) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by Dependencies.transactionsViewModel.state.collectAsStateWithLifecycle()
    val accountState by Dependencies.transactionsViewModel.accountState.collectAsStateWithLifecycle()
    val categoryState by Dependencies.transactionsViewModel.categoryState.collectAsStateWithLifecycle()
    onEvent(Event.TransactionsEvent.SetTransactionById(transactionId))
    onEvent(Event.TransactionsEvent.SetAccountId(accountId))
    onEvent(Event.TransactionsEvent.SetCategoryId(categoryId))

    val calendar = Calendar.getInstance()
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(Event.TransactionsEvent.SaveTransaction)
                navigate(OperationsNavigateCode.BACK)
            }) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Done,
                    contentDescription = "add"
                )
            }
        }
    ) { _ ->
        when {
            showDatePicker -> {
                DatePickerModal(
                    onDateSelected = { unixtime ->
                        unixtime?.let {
                            onEvent(Event.TransactionsEvent.SetDate(it))
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
                val sign = if (categoryState.type == CategoryTypes.EXPENSES) {
                    "-"
                } else {
                    "+"
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
                        onEvent(Event.TransactionsEvent.SetAmount(value))
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
                    onEvent(Event.TransactionsEvent.SetNotes(it))
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
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

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