package com.qosquo.wallet.presentation.ui.operations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.qosquo.wallet.Dependencies
import com.qosquo.wallet.domain.Colors
import com.qosquo.wallet.domain.Currencies
import com.qosquo.wallet.domain.Icons
import com.qosquo.wallet.domain.Transaction
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

@Composable
fun OperationsList(
    navigate: (transactionId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by Dependencies.transactionsViewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
        ) {
            items(state.transactionsPerDay.keys.toList()) { unixtime ->
                val date = Calendar.getInstance()
                date.timeInMillis = unixtime
                ListItem(
                    headlineContent = {
                        date.getDisplayName(
                            Calendar.DAY_OF_WEEK,
                            Calendar.LONG,
                            Locale.getDefault()
                        )?.let {
                            Text(text = it)
                        }
                    },
                    supportingContent = {
                        val month = date.getDisplayName(
                            Calendar.MONTH,
                            Calendar.LONG,
                            Locale.getDefault()
                        )
                        val year = date.get(Calendar.YEAR)
                        month?.let {
                            Text("$month $year")
                        }
                    },
                    leadingContent = {
                        Text(
                            text = date.get(Calendar.DATE).toString(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    trailingContent = {
                        state.transactionsPerDay[unixtime]?.let { transactions ->
                            val currencySumMap: MutableMap<Int, Double> = emptyMap<Int, Double>().toMutableMap()
                            Currencies.entries.forEach { currency ->
                                val sumOfCurrency = transactions.sumOf {
                                    if (it.accountCurrency == currency.ordinal) {
                                        it.amount.toDouble()
                                    } else {
                                        0.toDouble()
                                    }
                                }
                                currencySumMap[currency.ordinal] = sumOfCurrency
                            }
                            val sortedCurrencySumMap = currencySumMap.toSortedMap(
                                compareBy<Int> { currencySumMap[it] }.thenBy { it }
                            )
                            Column(
                                modifier = modifier,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                val mapKeys = sortedCurrencySumMap.keys.toList()
                                val firstCurrency = mapKeys[0]
                                sortedCurrencySumMap[firstCurrency]?.let {
                                    val text = amountToStringWithCurrency(
                                        amount = it.toFloat(),
                                        currency = Currencies.entries[firstCurrency]
                                    )
                                    Text(
                                        text = text,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                val secondCurrency = mapKeys[1]
                                sortedCurrencySumMap[secondCurrency]?.let {
                                    val text = amountToStringWithCurrency(
                                        amount = it.toFloat(),
                                        currency = Currencies.entries[secondCurrency]
                                    )
                                    if (abs(it) >= 0.01) {
                                        Text(
                                            text = text,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
                HorizontalDivider()
                LazyColumn(
                    modifier = modifier
                        .heightIn(max = 9999.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    state.transactionsPerDay[unixtime]?.let {
                        items(it) { item ->
                            ListItem(
                                headlineContent = {
                                    Text(text = item.categoryName)
                                },
                                overlineContent = {
                                    Text(text = item.accountName)
                                },
                                supportingContent = {
                                    if (item.notes.isNotEmpty()) {
                                        Text(text = item.notes)
                                    }
                                },
                                leadingContent = {
                                    Box(
                                        modifier = modifier
                                            .size(36.dp)
                                            .wrapContentSize(align = Alignment.Center)
                                            .clip(CircleShape)
                                            .background(Color(item.colorHex.toColorInt()))
                                    ) {

                                        Icon(
                                            imageVector = ImageVector.vectorResource(
                                                id = item.categoryIconId
                                            ),
                                            contentDescription = "bus",
                                            tint = Color.White,
                                            modifier = modifier
                                                .fillMaxSize()
                                                .padding(4.dp)
                                        )
                                    }
                                },
                                trailingContent = {
                                    val text = amountToStringWithCurrency(
                                        amount = item.amount,
                                        currency = Currencies.entries[item.accountCurrency]
                                    )
                                    Text(
                                        text = text,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                modifier = modifier
                                    .clickable {
                                        navigate(item.id)
                                    }
                            )
                            HorizontalDivider(modifier = modifier.padding(horizontal = 8.dp))
                        }
                    }

                }
            }
        }
    }
}