package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.LikeSource

class LikeRepository : Repository by Repository() {
    private val source = LikeSource

    suspend fun giveLike(userId: Long, articleId: Long): DataResponse<Unit> {
        return withDispatcher { source.giveLike(userId, articleId) }
    }

    suspend fun cancelLike(userId: Long, articleId: Long): DataResponse<Unit> {
        return withDispatcher { source.cancelLike(userId, articleId) }
    }

    suspend fun existsLike(userId: Long, articleId: Long): DataResponse<Boolean> {
        return withDispatcher { source.existsLike(userId, articleId) }
    }
}