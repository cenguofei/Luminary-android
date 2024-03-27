package com.example.lunimary.models.source.remote

import com.example.lunimary.models.ext.FollowInfo
import com.example.lunimary.models.ext.FollowersInfo
import com.example.lunimary.models.ext.UserFriend
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.RelationResponse

interface UserDetailSource {
    suspend fun likesOfUser(): DataResponse<Long>

    suspend fun followings(): RelationResponse<FollowInfo>

    suspend fun followers(): RelationResponse<FollowersInfo>

    suspend fun mutualFollowUsers(): RelationResponse<UserFriend>
}