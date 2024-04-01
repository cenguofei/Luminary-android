package com.example.lunimary.ui.home

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article

@Composable
fun FriendsArticlePage(
    recommendViewModel: RecommendViewModel,
    onItemClick: (Article) -> Unit,
    friendsArticles: LazyPagingItems<Article>
) {
    LunimaryPagingContent(
        items = friendsArticles,
        key = { friendsArticles[it]?.id!! },
    ) {
        ArticleItem(onItemClick = onItemClick, article = it)
    }
}