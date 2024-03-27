package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.BaseRepository
import com.example.lunimary.models.ext.FollowInfo
import com.example.lunimary.models.ext.FollowersInfo
import com.example.lunimary.models.ext.UserFriend
import com.example.lunimary.models.responses.RelationResponse
import com.example.lunimary.models.source.remote.impl.RelationSourceImpl
import com.example.lunimary.util.currentUser

class RelationRepository : BaseRepository by BaseRepository() {
    private val relationSource = RelationSourceImpl()

    suspend fun followings(): RelationResponse<FollowInfo> {
        return withDispatcher { relationSource.followings(false, currentUser.id) }
    }

    suspend fun followers(): RelationResponse<FollowersInfo> {
        return withDispatcher { relationSource.followers(false, currentUser.id) }
    }

    suspend fun mutualFollowUsers(): RelationResponse<UserFriend> {
        return withDispatcher { relationSource.mutualFollowUsers(false) }
    }
}