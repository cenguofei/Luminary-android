package com.example.lunimary.ui.user

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.model.Article
import com.example.lunimary.ui.edit.EditType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutArticles(
    coroutineScope: CoroutineScope,
    userDetailViewModel: UserDetailViewModel,
    onDraftClick: () -> Unit,
    onItemClick: (PageItem<Article>) -> Unit,
    navToEdit: (EditType, PageItem<Article>) -> Unit
) {
    val tabs = userDetailViewModel.tabs
    val pagerState = userDetailViewModel.pagerState
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = Color.Transparent,
        divider = {},
        edgePadding = 0.dp
    ) {
        tabs.forEachIndexed { index, tab ->
            TextButton(
                onClick = {
                    if (pagerState.currentPage != index) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
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
            onDraftClick = onDraftClick,
            onItemClick = onItemClick,
            navToEdit = navToEdit
        )
    }
}

