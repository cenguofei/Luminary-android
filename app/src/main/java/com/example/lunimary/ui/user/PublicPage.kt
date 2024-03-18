package com.example.lunimary.ui.user

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.lunimary.design.LunimaryScreen
import com.example.lunimary.models.Article
import com.example.lunimary.models.source.local.articleDao
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.ui.home.ArticleItem
import com.example.lunimary.ui.home.ArticleItemContainerColor
import com.example.lunimary.ui.user.draft.DraftItem
import com.example.lunimary.util.currentUser


@Composable
fun PublicPage(
    userDetailViewModel: UserDetailViewModel,
    onItemClick: (Article) -> Unit,
    modifier: Modifier,
    onDraftClick: () -> Unit
) {
    val drafts = articleDao.findArticlesByUsername(currentUser.username).observeAsState()
    LaunchedEffect(
        key1 = userDetailViewModel,
        block = {
            userDetailViewModel.publicArticles()
        }
    )
    val compositionsState = userDetailViewModel.publicArticles.observeAsState()
    LunimaryScreen(
        modifier = modifier,
        error = compositionsState.value is NetworkResult.Error,
        empty = compositionsState.value?.data.isNullOrEmpty(),
        shimmer = compositionsState.value is NetworkResult.Loading,
        errorMsg = (compositionsState.value as? NetworkResult.Error)?.msg,
        emptyMsg = (compositionsState.value as? NetworkResult.Empty)?.msg,
    ) {
        val data = compositionsState.value?.data ?: emptyList()
        LazyColumn(modifier = Modifier) {
            if (drafts.value?.isNotEmpty() == true) {
                item {
                    DraftItem(
                        articles = drafts.value ?: emptyList(),
                        onClick = { onDraftClick() },
                        showDraftLabel = true
                    )
                }
            }
            items(data.count()) {
                ArticleItem(
                    onItemClick = onItemClick,
                    article = data[it],
                    containerColor = ArticleItemContainerColor.Default.copy(
                        normalColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.15f)
                    )
                )
            }
        }
    }
}
