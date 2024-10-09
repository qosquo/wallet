package com.qosquo.wallet.ui.screens.categories

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Tab
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qosquo.wallet.Dependencies
import com.qosquo.wallet.Event
import com.qosquo.wallet.ui.screens.Screens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CategoriesList(
    onNavigate: (id: Long?) -> Unit
) {
    val state by Dependencies.categoriesViewModel.state.collectAsStateWithLifecycle()

    val tabRows = listOf(
        "Expenses",
        "Income"
    )
    val pagerState = rememberPagerState {
        tabRows.size
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onNavigate(null)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        }
    ) {
        var selectedTabIndex by remember {
            mutableIntStateOf(0)
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val scope = rememberCoroutineScope()

            LaunchedEffect(pagerState.currentPage) {
                selectedTabIndex = pagerState.currentPage
            }
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabRows.forEachIndexed {index, text ->
                    Tab(
                        selected = index == selectedTabIndex,
                        text = {
                            Text(text = text)
                        },
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                            selectedTabIndex = index
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize()
                )  {
                    items(state.categories) {

                    }
                }
            }

        }

    }
}