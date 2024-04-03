package com.example.lunimary.ui.home

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article

@Composable
fun FriendsArticlePage(
    onItemClick: (Article) -> Unit,
    friendsArticles: LazyPagingItems<Article>
) {
    LunimaryPagingContent(
        items = friendsArticles,
        key = { friendsArticles[it]?.id!! },
    ) { _, item ->
        ArticleItem(onItemClick = onItemClick, article = item)
    }
}