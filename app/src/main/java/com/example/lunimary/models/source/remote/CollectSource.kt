package com.example.lunimary.models.source.remote

import com.example.lunimary.models.responses.DataResponse

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
}