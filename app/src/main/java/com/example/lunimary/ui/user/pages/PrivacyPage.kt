package com.example.lunimary.ui.user.pages

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.nicepage.LunimaryPagingContent
import com.example.lunimary.model.Article
import com.example.lunimary.ui.common.ArticleItem

@Composable
fun PrivacyPage(
    modifier: Modifier,
    onItemClick: (PageItem<Article>) -> Unit,
    privacyArticlesState: LazyPagingItems<PageItem<Article>>
) {
    LunimaryPagingContent(
        modifier = modifier,
        items = privacyArticlesState,
        key = { privacyArticlesState[it]?.data?.id!! },
    ) { _, item ->
        ArticleItem(
            onItemClick = onItemClick,
            articlePageItem = item
        )
    }
}
