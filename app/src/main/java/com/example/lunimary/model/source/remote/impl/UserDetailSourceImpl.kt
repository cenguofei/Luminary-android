package com.example.lunimary.model.source.remote.impl

import com.example.lunimary.base.currentUser
import com.example.lunimary.base.ktor.addPathParam
import com.example.lunimary.base.ktor.init
import com.example.lunimary.model.ext.FollowInfo
import com.example.lunimary.model.ext.FollowersInfo
import com.example.lunimary.model.ext.InteractionData
import com.example.lunimary.model.ext.UserFriend
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.responses.RelationResponse
import com.example.lunimary.model.source.remote.RelationSource
import com.example.lunimary.model.source.remote.UserDetailSource
import com.example.lunimary.util.interactionDataPath
import com.example.lunimary.util.likesOfUserPath
import io.ktor.client.request.get

class UserDetailSourceImpl : BaseSourceImpl by BaseSourceImpl(), UserDetailSource {
    private val delegate = RelationSource
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

    override suspend fun interactionData(): DataResponse<InteractionData> {
        return client.get(urlString = interactionDataPath){
            addPathParam(currentUser.id)
        }.init()
    }
}