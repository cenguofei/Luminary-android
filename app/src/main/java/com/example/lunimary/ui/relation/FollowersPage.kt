package com.example.lunimary.ui.relation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.User
import com.example.lunimary.base.network.NetworkResult

@Composable
fun FollowersPage(
    relationViewModel: RelationViewModel,
    onItemClick: (User) -> Unit
) {
    val followers = relationViewModel.followers.collectAsLazyPagingItems()
    LunimaryPagingContent(
        items = followers,
        topItem = { Spacer(modifier = Modifier.height(16.dp)) },
        viewModel = relationViewModel,
        pagingKey = "FollowersPage"
    ) {
        Column {
            val state: MutableState<NetworkResult<Unit>> = remember {
                mutableStateOf(NetworkResult.None())
            }
            FollowerItem(
                followersInfo = it,
                onMoreClick = {},
                onReturnFollowClick = {
                    relationViewModel.onFollowClick(it.follower.id, state)
                },
                state = state,
                onCancelFollowClick = {
                    relationViewModel.onUnfollowClick(it.follower.id, state = state)
                },
                onItemClick = onItemClick
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}