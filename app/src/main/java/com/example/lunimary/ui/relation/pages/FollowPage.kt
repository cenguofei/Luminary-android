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
import com.example.lunimary.model.ext.FollowInfo
import com.example.lunimary.ui.relation.FollowItem
import com.example.lunimary.ui.relation.FollowItemData
import com.example.lunimary.ui.relation.RelationViewModel
import com.example.lunimary.util.logi

@Composable
fun FollowPage(
    onItemClick: (User) -> Unit,
    followings: LazyPagingItems<PageItem<FollowInfo>>,
    relationViewModel: RelationViewModel
) {
    LaunchedEffect(
        key1 = Unit,
        block = {
            followings.refresh()
            "followings.refresh".logi("relation_refresh")
        }
    )
    LunimaryPagingContent(
        items = followings
    ) { _, item ->
        val state: MutableState<NetworkResult<Unit>> = remember {
            mutableStateOf(NetworkResult.None())
        }
        FollowItem(
            followInfoData = FollowItemData(
                followInfo = item.data,
                cancelFollow = false
            ),
            onMoreClick = {},
            onFollowClick = {
                relationViewModel.onFollowClick(item.data.myFollow.id, state)
            },
            onCancelFollowClick = {
                relationViewModel.onUnfollowClick(item.data.myFollow.id, state)
            },
            state = state,
            onItemClick = onItemClick,
        )
    }
}