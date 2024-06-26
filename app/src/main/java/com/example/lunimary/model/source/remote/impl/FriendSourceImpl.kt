package com.example.lunimary.model.source.remote.impl

import com.example.lunimary.base.ktor.addPathParam
import com.example.lunimary.base.ktor.init
import com.example.lunimary.base.ktor.securityGet
import com.example.lunimary.base.ktor.securityPost
import com.example.lunimary.base.ktor.setJsonBody
import com.example.lunimary.model.ExistingFriendship
import com.example.lunimary.model.Friend
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.FriendSource
import com.example.lunimary.util.existingFriendshipPath
import com.example.lunimary.util.followPath
import com.example.lunimary.util.invisibleFollowPath
import com.example.lunimary.util.unfollowPath
import io.ktor.client.request.post

class FriendSourceImpl : BaseSourceImpl by BaseSourceImpl(), FriendSource {
    override suspend fun follow(meId: Long, whoId: Long): DataResponse<Unit> {
        val friend = Friend(userId = meId, whoId = whoId)
        return client.securityPost(urlString = followPath) {
            setJsonBody(friend)
        }.init()
    }

    override suspend fun unfollow(whoId: Long): DataResponse<Unit> {
        return client.securityPost(urlString = unfollowPath) {
           addPathParam(whoId)
        }.init()
    }

    override suspend fun existingFriendship(
        meId: Long,
        whoId: Long
    ): DataResponse<ExistingFriendship> {
        return client.post(urlString = existingFriendshipPath){
            setJsonBody(Friend(userId = meId, whoId = whoId))
        }.init()
    }

    override suspend fun invisibleFollow(followerId: Long): DataResponse<Boolean> {
        return client.securityGet(urlString = invisibleFollowPath){
            addPathParam(followerId)
        }.init()
    }
}
