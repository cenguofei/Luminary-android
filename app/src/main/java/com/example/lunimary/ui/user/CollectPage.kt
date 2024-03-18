package com.example.lunimary.ui.user

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.lunimary.design.LunimaryScreen
import com.example.lunimary.models.Article
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.ui.home.ArticleItem
import com.example.lunimary.ui.home.ArticleItemContainerColor

@Composable
fun CollectPage(
    userDetailViewModel: UserDetailViewModel,
    modifier: Modifier,
    onItemClick: (Article) -> Unit
) {
    LaunchedEffect(
        key1 = userDetailViewModel,
        block = {
            userDetailViewModel.collectsOfUser()
        }
    )
    val collectsOfUser = userDetailViewModel.collectsOfUser.observeAsState()
    LunimaryScreen(
        modifier = modifier,
        error = collectsOfUser.value is NetworkResult.Error,
        errorMsg = (collectsOfUser.value as? NetworkResult.Error)?.msg,
        empty = collectsOfUser.value?.data.isNullOrEmpty(),
        emptyMsg = (collectsOfUser.value as? NetworkResult.Empty)?.msg,
        shimmer = collectsOfUser.value is NetworkResult.Loading,
    ) {
        val data = collectsOfUser.value?.data ?: emptyList()
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
