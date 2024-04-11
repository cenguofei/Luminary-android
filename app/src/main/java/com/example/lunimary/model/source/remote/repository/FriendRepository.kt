package com.example.lunimary.model.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.model.ExistingFriendship
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.FriendSource

class FriendRepository: Repository by Repository() {
    private val friendSource = FriendSource

    suspend fun follow(meId: Long, whoId: Long): DataResponse<Unit> =
        withDispatcher { friendSource.follow(meId, whoId) }

    suspend fun unfollow(whoId: Long): DataResponse<Unit> =
        withDispatcher { friendSource.unfollow(whoId) }

    suspend fun existingFriendship(
        meId: Long,
        whoId: Long
    ): DataResponse<ExistingFriendship> =
        withDispatcher { friendSource.existingFriendship(meId, whoId) }
}