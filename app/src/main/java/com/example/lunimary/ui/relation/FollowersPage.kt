package com.example.lunimary.ui.relation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.nicepage.LunimaryPagingContent
import com.example.lunimary.models.User
import com.example.lunimary.models.ext.FollowersInfo
import com.example.lunimary.util.logi

@Composable
fun FollowersPage(
    relationViewModel: RelationViewModel,
    onItemClick: (User) -> Unit,
    followers: LazyPagingItems<PageItem<FollowersInfo>>
) {
    LaunchedEffect(
        key1 = Unit,
        block = {
            followers.refresh()
            "followers.refresh".logi("relation_refresh")
        }
    )
    LunimaryPagingContent(
        items = followers,
        topItem = { Spacer(modifier = Modifier.height(16.dp)) }
    ) { _, item ->
        Column {
            val state: MutableState<NetworkResult<Unit>> = remember {
                mutableStateOf(NetworkResult.None())
            }
            FollowerItem(
                followersInfo = item.data,
                onMoreClick = {},
                onReturnFollowClick = {
                    relationViewModel.onFollowClick(item.data.follower.id, state)
                },
                state = state,
                onCancelFollowClick = {
                    relationViewModel.onUnfollowClick(item.data.follower.id, state = state)
                },
                onItemClick = onItemClick,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}