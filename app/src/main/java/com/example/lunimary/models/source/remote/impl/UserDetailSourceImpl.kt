package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.models.ext.FollowInfo
import com.example.lunimary.models.ext.FollowersInfo
import com.example.lunimary.models.ext.UserFriend
import com.example.lunimary.base.ktor.addPathParam
import com.example.lunimary.base.ktor.init
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.RelationResponse
import com.example.lunimary.models.source.remote.RelationSource
import com.example.lunimary.models.source.remote.UserDetailSource
import com.example.lunimary.util.likesOfUserPath
import io.ktor.client.request.get

class UserDetailSourceImpl : BaseSourceImpl by BaseSourceImpl(), UserDetailSource {
    private val delegate: RelationSource = RelationSourceImpl()
    override suspend fun likesOfUser(userId: Long): DataResponse<Long> {
        return client.get(urlString = likesOfUserPath) {
            addPathParam(userId)
        }.init()
    }

    override suspend fun followings(userId: Long): RelationResponse<FollowInfo> =
        delegate.followings(userId = userId)

    override suspend fun followers(userId: Long): RelationResponse<FollowersInfo> =
        delegate.followers(userId = userId)

    override suspend fun mutualFollowUsers(): RelationResponse<UserFriend> =
        delegate.mutualFollowUsers()
}