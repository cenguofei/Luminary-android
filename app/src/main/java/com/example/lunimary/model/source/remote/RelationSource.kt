package com.example.lunimary.model.source.remote

import com.example.lunimary.model.ext.FollowInfo
import com.example.lunimary.model.ext.FollowersInfo
import com.example.lunimary.model.ext.UserFriend
import com.example.lunimary.model.responses.RelationResponse
import com.example.lunimary.model.source.remote.impl.RelationSourceImpl

interface RelationSource {
    suspend fun followings(
        onlyNum: Boolean = true,
        userId: Long
    ): RelationResponse<FollowInfo>

    suspend fun followers(
        onlyNum: Boolean = true,
        userId: Long
    ): RelationResponse<FollowersInfo>

    suspend fun mutualFollowUsers(
        onlyNum: Boolean = true,
    ): RelationResponse<UserFriend>

    companion object : RelationSource by RelationSourceImpl()
}