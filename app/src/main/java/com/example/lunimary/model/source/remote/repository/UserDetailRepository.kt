package com.example.lunimary.model.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.model.ext.FollowInfo
import com.example.lunimary.model.ext.FollowersInfo
import com.example.lunimary.model.ext.InteractionData
import com.example.lunimary.model.ext.UserFriend
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.responses.RelationResponse
import com.example.lunimary.model.source.remote.UserDetailSource

class UserDetailRepository : Repository by Repository() {
    private val userDetailSource = UserDetailSource
    suspend fun likesOfUser(userId: Long): DataResponse<Long> =
        withDispatcher { userDetailSource.likesOfUser(userId) }

    suspend fun followings(userId: Long): RelationResponse<FollowInfo> =
        withDispatcher { userDetailSource.followings(userId = userId) }

    suspend fun followers(userId: Long): RelationResponse<FollowersInfo> =
        withDispatcher { userDetailSource.followers(userId = userId) }

    suspend fun mutualFollowUsers(): RelationResponse<UserFriend> =
        withDispatcher { userDetailSource.mutualFollowUsers() }

    suspend fun interactionData(): DataResponse<InteractionData> =
        withDispatcher { userDetailSource.interactionData() }
}