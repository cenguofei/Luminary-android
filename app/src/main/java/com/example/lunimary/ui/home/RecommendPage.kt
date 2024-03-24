package com.example.lunimary.ui.home

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.design.LunimaryPagingScreen
import com.example.lunimary.models.Article
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RecommendPage(
    recommendViewModel: RecommendViewModel,
    isOffline: StateFlow<Boolean>,
    onItemClick: (Article) -> Unit,
) {
    val articles = recommendViewModel.recommendArticles.collectAsLazyPagingItems()
    val offline = isOffline.collectAsStateWithLifecycle()

    LunimaryPagingScreen(
        items = articles,
        networkError = offline.value,
        key = { articles[it]?.id!! }
    ) {
        ArticleItem(onItemClick = onItemClick, article = it)
    }
}
