package com.example.lunimary.ui.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.currentUser
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article
import com.example.lunimary.models.source.local.articleDao
import com.example.lunimary.ui.home.ArticleItem
import com.example.lunimary.ui.user.draft.DraftItem


@Composable
fun PublicPage(
    onItemClick: (PageItem<Article>) -> Unit,
    modifier: Modifier,
    onDraftClick: () -> Unit,
    compositionsState: LazyPagingItems<PageItem<Article>>
) {
    val drafts = articleDao.findArticlesByUsername(currentUser.username).observeAsState()
    LunimaryPagingContent(
        modifier = modifier,
        items = compositionsState,
        key = { compositionsState[it]?.data?.id!! },
        topItem = {
            if (drafts.value?.isNotEmpty() == true) {
                DraftItem(
                    articles = drafts.value ?: emptyList(),
                    onClick = { onDraftClick() },
                    showDraftLabel = true
                )
            }
        }
    ) { _, item ->
        ArticleItem(
            onItemClick = onItemClick,
            articlePageItem = item
        )
    }
}
