package com.example.lunimary.ui.relation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.request
import com.example.lunimary.models.User
import com.example.lunimary.models.ext.FollowInfo
import com.example.lunimary.models.ext.FollowersInfo
import com.example.lunimary.models.source.remote.repository.FriendRepository
import com.example.lunimary.models.source.remote.repository.RelationRepository
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.util.currentUser

class RelationViewModel : BaseViewModel() {
    private val relationRepository = RelationRepository()
    private val friendRepository = FriendRepository()

    private val _friends: MutableState<NetworkResult<List<User>>> =
        mutableStateOf(NetworkResult.None())
    val friends: State<NetworkResult<List<User>>> get() = _friends

    private val _followings: MutableState<NetworkResult<List<FollowInfo>>> =
        mutableStateOf(NetworkResult.None())
    val followings: State<NetworkResult<List<FollowInfo>>> get() = _followings

    private val _followers: MutableState<NetworkResult<List<FollowersInfo>>> =
        mutableStateOf(NetworkResult.None())
    val followers: State<NetworkResult<List<FollowersInfo>>> get() = _followers

    init {
        mutualFollowUsers()
        followings()
        followers()
    }

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

    private fun mutualFollowUsers() {
        fly(FLY_MUTUAL_FOLLOW_USERS) {
            request(
                block = {
                    _friends.value = NetworkResult.Loading()
                    relationRepository.mutualFollowUsers()
                },
                onSuccess = { data, msg ->
                    val users = data?.relations?.map { it.user } ?: emptyList()
                    if (users.isEmpty()) {
                        _friends.value = NetworkResult.Empty(msg ?: "暂时没有相关的内容")
                    } else {
                        _friends.value = NetworkResult.Success(data = users)
                    }
                },
                onFailed = {
                    _friends.value = NetworkResult.Error(it)
                },
                onFinish = { land(FLY_MUTUAL_FOLLOW_USERS) }
            )
        }
    }

    private fun followers() {
        fly(FLY_FOLLOWERS) {
            request(
                block = {
                    _followers.value = NetworkResult.Loading()
                    relationRepository.followers()
                },
                onSuccess = { data, msg ->
                    if (data?.relations.isNullOrEmpty()) {
                        _followers.value = NetworkResult.Empty(msg ?: "暂时没有相关的内容")
                    } else {
                        _followers.value = NetworkResult.Success(data = data?.relations)
                    }
                },
                onFailed = {
                    _followers.value = NetworkResult.Error(it)
                },
                onFinish = { land(FLY_FOLLOWERS) }
            )
        }
    }

    private fun followings() {
        fly(FLY_FOLLOWINGS) {
            request(
                block = {
                    _followings.value = NetworkResult.Loading()
                    relationRepository.followings()
                },
                onSuccess = { data, msg ->
                    if (data?.relations.isNullOrEmpty()) {
                        _followings.value = NetworkResult.Empty(msg ?: "暂时没有相关的内容")
                    } else {
                        _followings.value = NetworkResult.Success(data = data?.relations)
                    }
                },
                onFailed = {
                    _followings.value = NetworkResult.Error(it)
                },
                onFinish = { land(FLY_FOLLOWINGS) }
            )
        }
    }
}

const val FLY_MUTUAL_FOLLOW_USERS = "fly_mutual_follow_users"
const val FLY_FOLLOWINGS = "fly_follows"
const val FLY_FOLLOWERS = "fly_follows"
const val FLY_RETURN_FOLLOW = "fly_return_follow"
const val FLY_CANCEL_FOLLOW = "fly_cancel_follow"



