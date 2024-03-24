package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.BaseRepository
import com.example.lunimary.models.ExistingFriendship
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.FriendSource
import com.example.lunimary.models.source.remote.impl.FriendSourceImpl

class FriendRepository: BaseRepository by BaseRepository(), FriendSource {
    private val friendSource: FriendSource = FriendSourceImpl()

    override suspend fun follow(meId: Long, whoId: Long): DataResponse<Unit> =
        withDispatcher { friendSource.follow(meId, whoId) }

    override suspend fun unfollow(whoId: Long): DataResponse<Unit> =
        withDispatcher { friendSource.unfollow(whoId) }

    override suspend fun existingFriendship(
        meId: Long,
        whoId: Long
    ): DataResponse<ExistingFriendship> =
        withDispatcher { friendSource.existingFriendship(meId, whoId) }
}