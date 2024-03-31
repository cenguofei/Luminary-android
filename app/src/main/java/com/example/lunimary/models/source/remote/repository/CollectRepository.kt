package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.CollectSource

class CollectRepository : Repository by Repository() {
    private val source = CollectSource

    suspend fun giveCollect(collectUserId: Long, articleId: Long): DataResponse<Unit> {
        return withDispatcher { source.giveCollect(collectUserId, articleId) }
    }

    suspend fun cancelCollect(collectUserId: Long, articleId: Long): DataResponse<Unit> {
        return withDispatcher { source.cancelCollect(collectUserId, articleId) }
    }

    suspend fun existsCollect(
        collectUserId: Long,
        articleId: Long
    ): DataResponse<Boolean> {
        return withDispatcher { source.existsCollect(collectUserId, articleId) }
    }
}