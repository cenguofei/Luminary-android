package com.example.lunimary.model.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.LikeSource

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

    suspend fun invisibleToUser(likeId: Long): DataResponse<Boolean> {
        return withDispatcher { source.invisibleToUser(likeId) }
    }
}