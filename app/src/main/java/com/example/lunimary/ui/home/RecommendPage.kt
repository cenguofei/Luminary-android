package com.example.lunimary.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article
import com.example.lunimary.ui.common.ArticleNavArguments

@Composable
fun RecommendPage(
    onItemClick: (PageItem<Article>) -> Unit,
    articles: LazyPagingItems<PageItem<Article>>,
) {
    LunimaryPagingContent(
        items = articles
    ) { _, item ->
        ArticleItem(onItemClick = onItemClick, articlePageItem = item)
    }
}
