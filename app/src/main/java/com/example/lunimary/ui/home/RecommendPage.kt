package com.example.lunimary.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article
import com.example.lunimary.util.logd

@Composable
fun RecommendPage(
    recommendViewModel: RecommendViewModel,
    onItemClick: (Article) -> Unit,
) {
    val articles = recommendViewModel.recommendArticles.collectAsLazyPagingItems()
    LunimaryPagingContent(
        items = articles,
        key = { articles[it]?.id!! },
        viewModel = recommendViewModel,
        pagingKey = "RecommendPage"
    ) {
        ArticleItem(onItemClick = onItemClick, article = it)
    }
}
