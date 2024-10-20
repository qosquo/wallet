package com.qosquo.wallet.presentation.ui.operations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qosquo.wallet.Dependencies
import com.qosquo.wallet.R
import com.qosquo.wallet.utils.amountToStringWithCurrency
import com.qosquo.wallet.domain.Currencies
import com.qosquo.wallet.domain.Icons
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationsList(
    navigate: (
        transactionId: Long?,
        transactionAccountId: Long?,
        transactionCategoryId: Long?,
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by Dependencies.transactionsViewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.operations_list))
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigate(null, null, null)
            }) {
                Icon(imageVector = androidx.compose.material.icons.Icons.Default.Add, contentDescription = "add")
            }
        },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(NavigationBarDefaults.windowInsets),
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                        // TODO: SUM ONLY BY APP'S MAIN CURRENCY
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
                                            .background(Color(item.categoryColorHex.toColorInt()))
                                    ) {

                                        Icon(
                                            imageVector = ImageVector.vectorResource(
                                                id = item.categoryIconId
                                            ),
                                            contentDescription = "bus",
                                            tint = MaterialTheme.colorScheme.onSurface,
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
                                        navigate(item.id, item.accountId, item.categoryId)
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