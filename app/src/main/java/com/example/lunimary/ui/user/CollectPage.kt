package com.example.lunimary.ui.user

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article
import com.example.lunimary.ui.home.ArticleItem

@Composable
fun CollectPage(
    modifier: Modifier,
    onItemClick: (Article) -> Unit,
    collectsOfUser: LazyPagingItems<Article>
) {
    LunimaryPagingContent(
        modifier = modifier,
        items = collectsOfUser,
        key = { collectsOfUser[it]?.id!! }
    ) { _, item ->
        ArticleItem(
            onItemClick = onItemClick,
            article = item
        )
    }
}
