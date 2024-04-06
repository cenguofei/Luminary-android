package com.example.lunimary.ui.home

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article

@Composable
fun FriendsArticlePage(
    onItemClick: (PageItem<Article>) -> Unit,
    friendsArticles: LazyPagingItems<PageItem<Article>>
) {
    LunimaryPagingContent(
        items = friendsArticles
    ) { _, item ->
        ArticleItem(onItemClick = onItemClick, articlePageItem = item)
    }
}