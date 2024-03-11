package com.example.lunimary.ui.user

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutArticles(
    coroutineScope: CoroutineScope,
    userDetailViewModel: UserDetailViewModel,
    onDraftClick: () -> Unit,
) {
    val tabs = remember {
        listOf(
            ArticlesType.Composition, ArticlesType.Privacy,
            ArticlesType.Collect, ArticlesType.Like
        )
    }
    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = Color.Transparent,
        divider = {},
        edgePadding = 0.dp
    ) {
        tabs.forEachIndexed { index, tab ->
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            ) {
                Text(
                    text = tab.tabName,
                    color = if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
    HorizontalPager(state = pagerState) {
        ArticlesTypeList(
            tabs = tabs,
            index = it,
            userDetailViewModel = userDetailViewModel,
            onDraftClick = onDraftClick
        )
    }
}

