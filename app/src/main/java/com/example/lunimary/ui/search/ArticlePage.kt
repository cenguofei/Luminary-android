package com.example.lunimary.ui.search

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.nicepage.LunimaryPagingContent
import com.example.lunimary.models.Article
import com.example.lunimary.ui.common.ArticleItem

@Composable
fun ArticlePage(
    onItemClick: (PageItem<Article>) -> Unit,
    articleItems: LazyPagingItems<PageItem<Article>>,
) {
    LunimaryPagingContent(
        items = articleItems,
        key = { articleItems[it]?.data?.id!! },
        shimmer = false,
        searchEmptyEnabled = true,
        refreshEnabled = false
    ) { _, item ->
        ArticleItem(onItemClick = onItemClick, articlePageItem = item)
    }
}