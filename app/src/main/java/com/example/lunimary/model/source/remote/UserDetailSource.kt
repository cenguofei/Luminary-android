package com.example.lunimary.model.source.remote

import com.example.lunimary.model.ext.FollowInfo
import com.example.lunimary.model.ext.FollowersInfo
import com.example.lunimary.model.ext.InteractionData
import com.example.lunimary.model.ext.UserFriend
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.responses.RelationResponse
import com.example.lunimary.model.source.remote.impl.UserDetailSourceImpl

interface UserDetailSource {
    suspend fun likesOfUser(userId: Long): DataResponse<Long>

    suspend fun followings(userId: Long): RelationResponse<FollowInfo>

    suspend fun followers(userId: Long): RelationResponse<FollowersInfo>

    suspend fun mutualFollowUsers(): RelationResponse<UserFriend>

    suspend fun interactionData(id: Long = 0L): DataResponse<InteractionData>

    companion object : UserDetailSource by UserDetailSourceImpl()
}