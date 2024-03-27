package com.example.lunimary.ui.search

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.design.LunimaryPagingScreen
import com.example.lunimary.models.Article
import com.example.lunimary.ui.home.ArticleItem
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ArticlePage(
    isOffline: StateFlow<Boolean>,
    onItemClick: (Article) -> Unit,
    articleItems: LazyPagingItems<Article>,
) {
    val offline = isOffline.collectAsStateWithLifecycle()
    LunimaryPagingScreen(
        items = articleItems,
        networkError = offline.value,
        key = { articleItems[it]?.id!! },
        shimmer = false,
        searchEmptyEnabled = true
    ) {
        ArticleItem(onItemClick = onItemClick, article = it)
    }
}