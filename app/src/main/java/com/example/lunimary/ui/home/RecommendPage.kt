package com.example.lunimary.ui.home

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article

@Composable
fun RecommendPage(
    recommendViewModel: RecommendViewModel,
    onItemClick: (Article) -> Unit,
    articles: LazyPagingItems<Article>,
) {
    LunimaryPagingContent(
        items = articles,
        key = { articles[it]?.id!! }
    ) {
        ArticleItem(onItemClick = onItemClick, article = it)
    }
}
