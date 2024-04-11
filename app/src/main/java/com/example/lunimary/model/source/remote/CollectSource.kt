package com.example.lunimary.model.source.remote

import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.impl.CollectSourceImpl

interface CollectSource {
    suspend fun giveCollect(
        collectUserId: Long,
        articleId: Long
    ): DataResponse<Unit>

    suspend fun cancelCollect(
        collectUserId: Long,
        articleId: Long
    ): DataResponse<Unit>

    suspend fun existsCollect(
        collectUserId: Long,
        articleId: Long
    ): DataResponse<Boolean>

    companion object : CollectSource by CollectSourceImpl()
}