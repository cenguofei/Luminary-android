package com.example.lunimary.ui.relation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.lunimary.design.LunimaryScreen
import com.example.lunimary.models.User
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.network.isCurrentlyConnected

@Composable
fun FollowPage(
    relationViewModel: RelationViewModel,
    onItemClick: (User) -> Unit
) {
    LunimaryScreen(
        networkResult = relationViewModel.followings.value,
        networkError = !LocalContext.current.isCurrentlyConnected()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            val data = relationViewModel.followings.value.data ?: emptyList()
            item { Spacer(modifier = Modifier.height(16.dp)) }
            items(data) {
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
}