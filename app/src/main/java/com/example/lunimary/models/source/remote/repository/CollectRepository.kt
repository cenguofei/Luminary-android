package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.BaseRepository
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.CollectSource
import com.example.lunimary.models.source.remote.impl.CollectSourceImpl

class CollectRepository : BaseRepository by BaseRepository(), CollectSource {
    private val source: CollectSource = CollectSourceImpl()

    override suspend fun giveCollect(collectUserId: Long, articleId: Long): DataResponse<Unit> {
        return withDispatcher { source.giveCollect(collectUserId, articleId) }
    }

    override suspend fun cancelCollect(collectUserId: Long, articleId: Long): DataResponse<Unit> {
        return withDispatcher { source.cancelCollect(collectUserId, articleId) }
    }

    override suspend fun existsCollect(
        collectUserId: Long,
        articleId: Long
    ): DataResponse<Boolean> {
        return withDispatcher { source.existsCollect(collectUserId, articleId) }
    }
}