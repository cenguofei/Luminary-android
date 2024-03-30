package com.example.lunimary.ui.home

import androidx.compose.runtime.Composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article

@Composable
fun FollowingPage(
    recommendViewModel: RecommendViewModel,
    onItemClick: (Article) -> Unit
) {
    val articles = recommendViewModel.friendsArticles.collectAsLazyPagingItems()
    LunimaryPagingContent(
        items = articles,
        key = { articles[it]?.id!! },
        viewModel = recommendViewModel,
        pagingKey = "FollowingPage"
    ) {
        ArticleItem(onItemClick = onItemClick, article = it)
    }
}