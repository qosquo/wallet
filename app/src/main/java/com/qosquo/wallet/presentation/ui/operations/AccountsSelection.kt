package com.qosquo.wallet.ui.screens.operations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import com.qosquo.wallet.domain.Currencies
import com.qosquo.wallet.utils.amountToStringWithCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsSelection(
    onSelect: (selection: Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by Dependencies.accountsViewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.accounts_list))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onSelect(null)
                    }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back button"
                        )
                    }
                }
            )
        },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(NavigationBarDefaults.windowInsets)
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(state.accounts) { account ->
                ListItem(
                    headlineContent = {
                        Text(text = account.name)
                    },
                    leadingContent = {
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
                    },
                    trailingContent = {
                        val currency = Currencies.entries[account.currency]
                        val balanceText = amountToStringWithCurrency(
                            amount = account.balance,
                            currency = currency
                        )
                        Text(text = balanceText)
                    },
                    modifier = modifier
                        .padding(vertical = 4.dp)
                        .clickable {
                            onSelect(account.id)
                        }
                )
                HorizontalDivider()
            }
        }
    }
}