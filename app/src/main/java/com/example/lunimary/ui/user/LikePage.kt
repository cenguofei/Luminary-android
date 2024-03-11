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
fun LikePage(
    userDetailViewModel: UserDetailViewModel,
    modifier: Modifier,
    onItemClick: () -> Unit
) {
    LaunchedEffect(
        key1 = userDetailViewModel,
        block = {
            userDetailViewModel.likesOfUser()
        }
    )
    val likesOfUser = userDetailViewModel.likesOfUser.observeAsState()
    LunimaryScreen(
        modifier = modifier,
        error = likesOfUser.value is NetworkResult.Error,
        errorMsg = (likesOfUser.value as? NetworkResult.Error)?.msg,
        empty = likesOfUser.value?.data.isNullOrEmpty(),
        emptyMsg = (likesOfUser.value as? NetworkResult.Empty)?.msg,
        shimmer = likesOfUser.value is NetworkResult.Loading,
    ) {
        val data = likesOfUser.value?.data ?: emptyList()
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


