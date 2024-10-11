package com.qosquo.wallet.ui.screens.operations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Done
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
import com.qosquo.wallet.model.Currencies
import com.qosquo.wallet.ui.Colors
import com.qosquo.wallet.ui.Icons

@Composable
fun OperationsList(
    navigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
        ) {
            Column(
                modifier = modifier
            ) {
                ListItem(
                    headlineContent = {
                        Text(text = "Friday")
                    },
                    supportingContent = {
                        Text("October 2024")
                    },
                    leadingContent = {
                        Text(
                            text = "11",
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
                Column(
                    modifier = modifier
                        .padding(horizontal = 8.dp)
                ) {
                    ListItem(
                        headlineContent = {
                            Text(text = "Транспорт")
                        },
                        supportingContent = {
                            Text(text = "Основной")
                        },
                        leadingContent = {
                            Box(modifier = modifier
                                .size(36.dp)
                                .wrapContentSize(align = Alignment.Center)
                                .clip(CircleShape)
                                .background(Color(Colors.RED.hex.toColorInt()))) {

                                Icon(
                                    imageVector = ImageVector.vectorResource(id = Icons.Categories.BUS.id),
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
                                text = "-41${Currencies.RUBEL.symbol}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        modifier = modifier
                            .clickable {

                            }
                    )
                    HorizontalDivider()
                }
            }

        }
    }
}