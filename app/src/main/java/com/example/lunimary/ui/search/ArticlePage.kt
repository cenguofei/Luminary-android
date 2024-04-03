package com.example.lunimary.ui.search

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article
import com.example.lunimary.ui.home.ArticleItem

@Composable
fun ArticlePage(
    onItemClick: (Article) -> Unit,
    articleItems: LazyPagingItems<Article>,
) {
    LunimaryPagingContent(
        items = articleItems,
        key = { articleItems[it]?.id!! },
        shimmer = false,
        searchEmptyEnabled = true,
        refreshEnabled = false
    ) { _, item ->
        ArticleItem(onItemClick = onItemClick, article = item)
    }
}