package com.example.lunimary.ui.relation.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.nicepage.LunimaryPagingContent
import com.example.lunimary.model.User
import com.example.lunimary.model.ext.FollowersInfo
import com.example.lunimary.ui.relation.FollowerItem
import com.example.lunimary.ui.relation.RelationViewModel
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
        items = followers
    ) { _, item ->
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
    }
}