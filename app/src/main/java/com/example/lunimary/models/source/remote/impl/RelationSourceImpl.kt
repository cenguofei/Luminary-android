package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.models.ext.FollowInfo
import com.example.lunimary.models.ext.FollowersInfo
import com.example.lunimary.models.ext.UserFriend
import com.example.lunimary.models.ktor.addPathParam
import com.example.lunimary.models.ktor.addQueryParam
import com.example.lunimary.models.ktor.init
import com.example.lunimary.models.responses.RelationResponse
import com.example.lunimary.models.source.remote.RelationSource
import com.example.lunimary.util.currentUser
import com.example.lunimary.util.mutualFollowFriendsPath
import com.example.lunimary.util.myFollowersPath
import com.example.lunimary.util.myFollowingsPath
import io.ktor.client.request.get

class RelationSourceImpl : BaseSourceImpl by BaseSourceImpl(), RelationSource {
    override suspend fun followings(
        onlyNum: Boolean,
        userId: Long
    ): RelationResponse<FollowInfo> {
        return client.get(
            urlString = myFollowingsPath,
        ) {
            addPathParam(userId)
            addQueryParam("onlyNum", onlyNum)
        }.init()
    }

    override suspend fun followers(
        onlyNum: Boolean,
        userId: Long
    ): RelationResponse<FollowersInfo> {
        return client.get(
            urlString = myFollowersPath
        ) {
            addPathParam(userId)
            addQueryParam("onlyNum", onlyNum)
        }.init()
    }

    override suspend fun mutualFollowUsers(onlyNum: Boolean): RelationResponse<UserFriend> {
        return client.get(urlString = mutualFollowFriendsPath) {
            addQueryParam("userId", currentUser.id)
            addQueryParam("onlyNum", onlyNum)
        }.init()
    }
}