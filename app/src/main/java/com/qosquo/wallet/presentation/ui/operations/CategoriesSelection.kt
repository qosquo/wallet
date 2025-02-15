package com.qosquo.wallet.ui.screens.operations

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.DrawableRes
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qosquo.wallet.Dependencies
import com.qosquo.wallet.R
import com.qosquo.wallet.presentation.ui.categories.CategoriesAction
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesSelection(
    onTabChange: (CategoriesAction.SetType) -> Unit,
    onSelect: (selection: Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by Dependencies.categoriesViewModel.state.collectAsStateWithLifecycle()

    val tabRows = listOf(
        "Expenses",
        "Income"
    )
    val pagerState = rememberPagerState {
        tabRows.size
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.categories_list))
                },
                scrollBehavior = scrollBehavior,
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
            .exclude(NavigationBarDefaults.windowInsets),
        modifier = modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
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
                onTabChange(CategoriesAction.SetType(pagerState.currentPage))
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
                            onTabChange(CategoriesAction.SetType(pagerState.currentPage))
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.fillMaxSize()
                )  {
                    val categories = if (index == 0) {
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
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .padding(4.dp)
                                    )
                                }
                            },
                            modifier = Modifier
                                .clickable {
                                    onSelect(category.id)
                                }
                        )
                        HorizontalDivider()
                    }
                }
            }

        }

    }
}