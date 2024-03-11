package com.example.lunimary.ui.user

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.lunimary.design.LunimaryScreen
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.ui.home.ArticleItem
import com.example.lunimary.ui.home.ArticleItemContainerColor

@Composable
fun PrivacyPage(
    userDetailViewModel: UserDetailViewModel,
    modifier: Modifier,
    onItemClick: () -> Unit
) {
    LaunchedEffect(
        key1 = userDetailViewModel,
        block = { userDetailViewModel.privacyArticles() }
    )
    val privacyArticlesState = userDetailViewModel.privacyArticles.observeAsState()
    LunimaryScreen(
        modifier = modifier,
        error = privacyArticlesState.value is NetworkResult.Error,
        empty = privacyArticlesState.value?.data.isNullOrEmpty(),
        shimmer = privacyArticlesState.value is NetworkResult.Loading,
        errorMsg = (privacyArticlesState.value as? NetworkResult.Error)?.msg,
        emptyMsg = (privacyArticlesState.value as? NetworkResult.Empty)?.msg,
    ) {
        val data = privacyArticlesState.value?.data ?: emptyList()
        LazyColumn(modifier = Modifier) {
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
