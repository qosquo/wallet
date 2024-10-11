package com.qosquo.wallet.presentation.ui.categories

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Tab
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.qosquo.wallet.domain.CategoryTypes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CategoriesList(
    onTabChange: (CategoriesAction.SetType) -> Unit,
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
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.categories_list))
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onNavigate(null)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(NavigationBarDefaults.windowInsets)
    ) { innerPadding ->
        var selectedTabIndex by remember {
            mutableIntStateOf(0)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val scope = rememberCoroutineScope()

            LaunchedEffect(pagerState.currentPage) {
                selectedTabIndex = pagerState.currentPage
                onTabChange(CategoriesAction.SetType(
                    CategoryTypes.entries[pagerState.currentPage]
                ))
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
                            onTabChange(CategoriesAction.SetType(
                                CategoryTypes.entries[pagerState.currentPage]
                            ))
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
                    val categories = if (selectedTabIndex == 0) {
                        state.expensesCategories
                    } else {
                        state.incomeCategories
                    }
                    items(categories) { category ->
                        ListItem(
                            headlineContent = {
                                Text(text = category.name)
                            },
                            leadingContent = {
                                Box(modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(category.colorHex.toColorInt()))) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(
                                            id = category.iconId
                                        ),
                                        contentDescription = "account icon",
                                        tint = Color.Black,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .padding(4.dp)
                                    )
                                }
                            },
                            modifier = Modifier
                                .clickable {
                                    onNavigate(category.id)
                                }
                        )
                    }
                }
            }

        }

    }
}