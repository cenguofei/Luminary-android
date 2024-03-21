package com.example.lunimary.models.source.remote

import com.example.lunimary.models.responses.DataResponse

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
}