package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.models.ExistingFriendship
import com.example.lunimary.models.Friend
import com.example.lunimary.models.ktor.addPathParam
import com.example.lunimary.models.ktor.init
import com.example.lunimary.models.ktor.securityPost
import com.example.lunimary.models.ktor.setJsonBody
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.FriendSource
import com.example.lunimary.util.existingFriendshipPath
import com.example.lunimary.util.followPath
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
}
