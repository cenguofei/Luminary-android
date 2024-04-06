package com.example.lunimary.ui.viewuser

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article
import com.example.lunimary.ui.home.ArticleItem
import java.util.UUID


@Composable
fun UserArticles(
    viewModel: ViewUserViewModel,
    onItemClick: (PageItem<Article>) -> Unit,
    modifier: Modifier
) {
    val userArticles = viewModel.userArticles.collectAsLazyPagingItems()
    LunimaryPagingContent(
        modifier = modifier,
        items = userArticles,
        key = { userArticles[it]?.data?.id ?: UUID.randomUUID() },
        viewModel = viewModel,
        pagingKey = "UserArticles_ViewUserViewModel"
    ) { _, item ->
        ArticleItem(
            onItemClick = onItemClick,
            articlePageItem = item
        )
    }
}
