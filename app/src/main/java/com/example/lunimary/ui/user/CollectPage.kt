package com.example.lunimary.ui.user

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article
import com.example.lunimary.ui.home.ArticleItem

@Composable
fun CollectPage(
    modifier: Modifier,
    onItemClick: (PageItem<Article>) -> Unit,
    collectsOfUser: LazyPagingItems<PageItem<Article>>
) {
    LunimaryPagingContent(
        modifier = modifier,
        items = collectsOfUser,
        key = { collectsOfUser[it]?.data?.id!! }
    ) { _, item ->
        ArticleItem(
            onItemClick = onItemClick,
            articlePageItem = item
        )
    }
}
