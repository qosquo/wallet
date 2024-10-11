package com.qosquo.wallet.presentation.ui.operations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.qosquo.wallet.domain.Colors
import com.qosquo.wallet.domain.Currencies
import com.qosquo.wallet.domain.Icons
import com.qosquo.wallet.domain.Transaction
import java.util.Calendar
import java.util.Locale

@Composable
fun OperationsList(
    navigate: (transactionId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val transactions = listOf(
        Transaction(
            id = 0,
            amount = -41F,
            accountName = "Основной",
            categoryName = "Транспорт",
            categoryIconId = Icons.Categories.BUS.id,
            colorHex = Colors.RED.hex,
            date = Calendar.getInstance().timeInMillis,
            notes = ""
        ),
        Transaction(
            id = 1,
            amount = -250F,
            accountName = "Основной",
            categoryName = "Еда",
            categoryIconId = Icons.Categories.HAMBURGER.id,
            colorHex = Colors.GREEN.hex,
            date = Calendar.getInstance().timeInMillis,
            notes = "Бургер и лимонад"
        ),
    )
    val dates: List<Long> = listOf(
        1728650537000,
        1728391337000,
        1728132137000,
        1727959337000,
    )

    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
        ) {
            items(dates) {
                val date = Calendar.getInstance()
                date.timeInMillis = it
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
                        Text(
                            text = "-291${Currencies.RUBEL.symbol}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
                HorizontalDivider()
                LazyColumn(
                    modifier = modifier
                        .heightIn(max = 9999.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    items(transactions) { item ->
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
                                Text(
                                    text = "${item.amount.toInt()}${Currencies.RUBEL.symbol}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            modifier = modifier
                                .clickable {
                                    navigate(item.id)
                                }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}