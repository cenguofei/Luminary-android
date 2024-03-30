package com.example.lunimary.ui.user

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.Article
import com.example.lunimary.models.source.local.articleDao
import com.example.lunimary.ui.home.ArticleItem
import com.example.lunimary.ui.home.ArticleItemContainerColor
import com.example.lunimary.ui.user.draft.DraftItem
import com.example.lunimary.base.currentUser


@Composable
fun PublicPage(
    userDetailViewModel: UserDetailViewModel,
    onItemClick: (Article) -> Unit,
    modifier: Modifier,
    onDraftClick: () -> Unit
) {
    val drafts = articleDao.findArticlesByUsername(currentUser.username).observeAsState()
    val compositionsState = userDetailViewModel.publicArticles.collectAsLazyPagingItems()
    LunimaryPagingContent(
        modifier = modifier,
        items = compositionsState,
        key = { compositionsState[it]?.id!! },
        topItem = {
            if (drafts.value?.isNotEmpty() == true) {
                DraftItem(
                    articles = drafts.value ?: emptyList(),
                    onClick = { onDraftClick() },
                    showDraftLabel = true
                )
            }
        },
        viewModel = userDetailViewModel,
        pagingKey = "PublicPage_userDetailViewModel"
    ) {
        ArticleItem(
            onItemClick = onItemClick,
            article = it,
            containerColor = ArticleItemContainerColor.Default.copy(
                normalColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.15f)
            )
        )
    }
}
