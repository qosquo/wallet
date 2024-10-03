package com.qosquo.wallet.ui.screens.categories

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qosquo.wallet.Dependencies
import com.qosquo.wallet.Event

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CategoriesList(
    onEvent: (Event.CategoriesEvent) -> Unit,
    onActionButtonClicked: (isEditing: Boolean) -> Unit
) {
    val state by Dependencies.categoriesViewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(Event.CategoriesEvent.ShowForm(null))
                onActionButtonClicked(false)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        }
    ) {
//        LazyColumn(
//            contentPadding = PaddingValues(16.dp),
//            modifier = Modifier.fillMaxSize()//.padding(padding)
//        )  {
//            items(state.categories) {
//
//            }
//        }
    }
}