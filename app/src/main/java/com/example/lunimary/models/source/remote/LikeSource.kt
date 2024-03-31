package com.example.lunimary.models.source.remote

import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.impl.LikeSourceImpl

interface LikeSource {
    suspend fun giveLike(
        userId: Long,
        articleId: Long
    ): DataResponse<Unit>

    suspend fun cancelLike(
        userId: Long,
        articleId: Long
    ): DataResponse<Unit>

    suspend fun existsLike(
        userId: Long,
        articleId: Long
    ): DataResponse<Boolean>

    companion object : LikeSource by LikeSourceImpl()
}