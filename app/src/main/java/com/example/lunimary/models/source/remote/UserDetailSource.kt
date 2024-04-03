package com.example.lunimary.models.source.remote

import com.example.lunimary.models.ext.FollowInfo
import com.example.lunimary.models.ext.FollowersInfo
import com.example.lunimary.models.ext.InteractionData
import com.example.lunimary.models.ext.UserFriend
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.RelationResponse
import com.example.lunimary.models.source.remote.impl.UserDetailSourceImpl

interface UserDetailSource {
    suspend fun likesOfUser(userId: Long): DataResponse<Long>

    suspend fun followings(userId: Long): RelationResponse<FollowInfo>

    suspend fun followers(userId: Long): RelationResponse<FollowersInfo>

    suspend fun mutualFollowUsers(): RelationResponse<UserFriend>

    suspend fun interactionData(): DataResponse<InteractionData>

    companion object : UserDetailSource by UserDetailSourceImpl()
}