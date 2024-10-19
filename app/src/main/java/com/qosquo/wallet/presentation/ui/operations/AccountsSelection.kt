package com.qosquo.wallet.ui.screens.operations

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qosquo.wallet.Dependencies
import com.qosquo.wallet.amountToStringWithCurrency
import com.qosquo.wallet.model.Currencies
import kotlinx.parcelize.Parcelize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsSelection(
    onSelect: (selection: Long) -> Unit
) {
    val state by Dependencies.accountsViewModel.state.collectAsStateWithLifecycle()
    Scaffold(
    ) { _ ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()//.padding(padding)
        ) {
            items(state.accounts) { account ->
                Card(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .clickable {
                            onSelect(account.id)
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(account.colorHex.toColorInt()))) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    id = account.accountIconId
                                ),
                                contentDescription = "account icon",
                                tint = Color.Black,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(4.dp)
                            )
                        }
                        Text(
                            text = account.name,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        )
                        val currency = Currencies.entries[account.currency]
                        val balanceText = amountToStringWithCurrency(
                            amount = account.balance,
                            currency = currency
                        )
                        Text(text = balanceText)
                    }
                }
            }
        }
    }
}