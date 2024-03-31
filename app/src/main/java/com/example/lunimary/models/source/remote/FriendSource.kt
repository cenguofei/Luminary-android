package com.example.lunimary.models.source.remote

import com.example.lunimary.models.ExistingFriendship
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.impl.FriendSourceImpl

interface FriendSource {
    /**
     * @param meId 我的id
     * @param whoId 要关注的用户的id
     */
    suspend fun follow(
        meId: Long,
        whoId: Long
    ): DataResponse<Unit>

    /**
     * @param whoId 取关谁
     */
    suspend fun unfollow(
        whoId: Long
    ): DataResponse<Unit>

    suspend fun existingFriendship(
        meId: Long,
        whoId: Long
    ): DataResponse<ExistingFriendship>

    companion object : FriendSource by FriendSourceImpl()
}