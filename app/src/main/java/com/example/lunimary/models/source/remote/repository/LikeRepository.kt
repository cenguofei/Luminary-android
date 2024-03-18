package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.BaseRepository
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.LikeSource
import com.example.lunimary.models.source.remote.impl.LikeSourceImpl

class LikeRepository : BaseRepository by BaseRepository(), LikeSource {
    private val source: LikeSource = LikeSourceImpl()

    override suspend fun giveLike(userId: Long, articleId: Long): DataResponse<Unit> {
        return withDispatcher { source.giveLike(userId, articleId) }
    }

    override suspend fun cancelLike(userId: Long, articleId: Long): DataResponse<Unit> {
        return withDispatcher { source.cancelLike(userId, articleId) }
    }

    override suspend fun existsLike(userId: Long, articleId: Long): DataResponse<Boolean> {
        return withDispatcher { source.existsLike(userId, articleId) }
    }
}