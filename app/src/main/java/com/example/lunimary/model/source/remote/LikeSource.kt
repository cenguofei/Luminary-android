package com.example.lunimary.model.source.remote

import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.impl.LikeSourceImpl

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