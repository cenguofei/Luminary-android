package com.example.lunimary.ui.relation.pages

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 12.dp, end = 12.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            onClick = {}
        ) {
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
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
    }
}