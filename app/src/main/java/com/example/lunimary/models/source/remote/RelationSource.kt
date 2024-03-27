package com.example.lunimary.models.source.remote

import com.example.lunimary.models.ext.FollowInfo
import com.example.lunimary.models.ext.FollowersInfo
import com.example.lunimary.models.ext.UserFriend
import com.example.lunimary.models.responses.RelationResponse

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
}