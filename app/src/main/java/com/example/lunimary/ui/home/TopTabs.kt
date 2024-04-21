package com.example.lunimary.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeTopBar(
    tabs: List<HomeCategories>,
    pagerState: PagerState,
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit,
    onHomeSortClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = onAddClick) {
            Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
        }
        TopTabs(
            modifier = Modifier.weight(1f),
            tabs = tabs,
            pagerState = pagerState,
            coroutineScope = coroutineScope,
            onHomeSortClick = onHomeSortClick
        )
        IconButton(onClick = onSearchClick) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopTabs(
    modifier: Modifier = Modifier,
    tabs: List<HomeCategories>,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    onHomeSortClick: () -> Unit
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = modifier,
        containerColor = Color.Transparent,
        divider = {}
    ) {
        tabs.forEachIndexed { index, tab ->
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            ) {
                val contentColor =
                    if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                Text(
                    text = tab.tabName,
                    color = contentColor
                )
                if (tab == HomeCategories.Recommend) {
                    Surface(
                        onClick = onHomeSortClick,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(50)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.size(18.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .width(16.dp)
                                    .height(2.dp)
                                    .background(contentColor)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Spacer(
                                modifier = Modifier
                                    .width(12.dp)
                                    .height(2.dp)
                                    .background(contentColor)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Spacer(
                                modifier = Modifier
                                    .width(6.dp)
                                    .height(2.dp)
                                    .background(contentColor)
                            )
                        }
                    }
                }
            }
        }
    }
}