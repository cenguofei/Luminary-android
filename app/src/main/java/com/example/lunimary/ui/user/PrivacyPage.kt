package com.example.lunimary.ui.user

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article
import com.example.lunimary.ui.home.ArticleItem

@Composable
fun PrivacyPage(
    modifier: Modifier,
    onItemClick: (Article) -> Unit,
    privacyArticlesState: LazyPagingItems<Article>
) {
    LunimaryPagingContent(
        modifier = modifier,
        items = privacyArticlesState,
        key = { privacyArticlesState[it]?.id!! },
    ) { _, item ->
        ArticleItem(
            onItemClick = onItemClick,
            article = item
        )
    }
}
