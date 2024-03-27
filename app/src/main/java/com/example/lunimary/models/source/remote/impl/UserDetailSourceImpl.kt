package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.models.ext.FollowInfo
import com.example.lunimary.models.ext.FollowersInfo
import com.example.lunimary.models.ext.UserFriend
import com.example.lunimary.models.ktor.addPathParam
import com.example.lunimary.models.ktor.init
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.RelationResponse
import com.example.lunimary.models.source.remote.RelationSource
import com.example.lunimary.models.source.remote.UserDetailSource
import com.example.lunimary.util.currentUser
import com.example.lunimary.util.likesOfUserPath
import io.ktor.client.request.get

class UserDetailSourceImpl: BaseSourceImpl by BaseSourceImpl(),  UserDetailSource {
    private val delegate: RelationSource = RelationSourceImpl()
    override suspend fun likesOfUser(): DataResponse<Long> {
        return client.get(urlString = likesOfUserPath) {
            addPathParam(currentUser.id)
        }.init()
    }

    override suspend fun followings(): RelationResponse<FollowInfo> = delegate.followings()

    override suspend fun followers(): RelationResponse<FollowersInfo> = delegate.followers()

    override suspend fun mutualFollowUsers(): RelationResponse<UserFriend> = delegate.mutualFollowUsers()
}