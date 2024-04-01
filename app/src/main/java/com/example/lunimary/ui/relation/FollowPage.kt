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
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.User
import com.example.lunimary.models.ext.FollowInfo

@Composable
fun FollowPage(
    onItemClick: (User) -> Unit,
    followings: LazyPagingItems<FollowInfo>,
    relationViewModel: RelationViewModel
) {
    LunimaryPagingContent(
        items = followings,
        topItem = { Spacer(modifier = Modifier.height(16.dp)) },
    ) {
        Column {
            val state: MutableState<NetworkResult<Unit>> = remember {
                mutableStateOf(NetworkResult.None())
            }
            FollowItem(
                followInfoData = FollowItemData(
                    followInfo = it,
                    cancelFollow = false
                ),
                onMoreClick = {},
                onFollowClick = {
                    relationViewModel.onFollowClick(it.myFollow.id, state)
                },
                onCancelFollowClick = {
                    relationViewModel.onUnfollowClick(it.myFollow.id, state)
                },
                state = state,
                onItemClick = onItemClick
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}