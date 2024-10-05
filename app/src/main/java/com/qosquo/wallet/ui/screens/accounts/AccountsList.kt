package com.qosquo.wallet.ui.screens.accounts

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.qosquo.wallet.Event
import com.qosquo.wallet.ui.screens.Screens
import com.qosquo.wallet.viewmodel.states.AccountsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsList(
//    state: AccountsState,
    onEvent: (Event.AccountsEvent) -> Unit,
    onActionButtonClicked: (isEditing: Boolean) -> Unit
) {
    val state by Dependencies.accountsViewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(Event.AccountsEvent.ShowForm(null))
                onActionButtonClicked(false)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        }
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
                            onEvent(Event.AccountsEvent.ShowForm(account))
                            onActionButtonClicked(true)
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
                        Text(text = "${account.balance.toInt()} ₽")
                    }
                }
            }
        }
    }
}