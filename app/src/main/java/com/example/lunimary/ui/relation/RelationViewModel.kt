package com.example.lunimary.ui.relation

import androidx.compose.runtime.MutableState
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.pager.pagerFlow
import com.example.lunimary.models.source.remote.paging.MyFollowersSource
import com.example.lunimary.models.source.remote.paging.MyFollowingsSource
import com.example.lunimary.models.source.remote.paging.MyFriendsSource
import com.example.lunimary.models.source.remote.repository.FriendRepository
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.currentUser

class RelationViewModel : BaseViewModel() {
    private val friendRepository = FriendRepository()

    val friends = pagerFlow { MyFriendsSource }

    val followings = pagerFlow { MyFollowingsSource }

    val followers = pagerFlow { MyFollowersSource }

    fun onFollowClick(
        whoId: Long,
        state: MutableState<NetworkResult<Unit>>
    ) {
        fly(FLY_RETURN_FOLLOW) {
            request(
                block = {
                    state.value = NetworkResult.Loading()
                    friendRepository.follow(currentUser.id, whoId)
                },
                onSuccess = { _, _ ->
                    state.value = NetworkResult.Success()
                },
                onFailed = {
                    state.value = NetworkResult.Error()
                },
                onFinish = { land(FLY_RETURN_FOLLOW) }
            )
        }
    }

    fun onUnfollowClick(
        whoId: Long,
        state: MutableState<NetworkResult<Unit>>
    ) {
        fly(FLY_CANCEL_FOLLOW) {
            request(
                block = {
                    state.value = NetworkResult.Loading()
                    friendRepository.unfollow(whoId)
                },
                onSuccess = { _, _ ->
                    state.value = NetworkResult.Success()
                },
                onFailed = {
                    state.value = NetworkResult.Error()
                },
                onFinish = { land(FLY_CANCEL_FOLLOW) }
            )
        }
    }
}

const val FLY_RETURN_FOLLOW = "fly_return_follow"
const val FLY_CANCEL_FOLLOW = "fly_cancel_follow"



