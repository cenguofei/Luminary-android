package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.models.Collect
import com.example.lunimary.models.Like
import com.example.lunimary.models.ktor.init
import com.example.lunimary.models.ktor.securityPost
import com.example.lunimary.models.ktor.setJsonBody
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.CollectSource
import com.example.lunimary.util.cancelCollectPath
import com.example.lunimary.util.cancelLikePath
import com.example.lunimary.util.createCollectPath
import com.example.lunimary.util.createLikePath
import com.example.lunimary.util.existsCollectPath
import com.example.lunimary.util.existsLikePath
import io.ktor.client.request.post

class CollectSourceImpl : BaseSourceImpl by BaseSourceImpl(), CollectSource {
    override suspend fun giveCollect(collectUserId: Long, articleId: Long): DataResponse<Unit> {
        return client.securityPost(urlString = createCollectPath) {
            setJsonBody(Collect(collectUserId = collectUserId, articleId = articleId))
        }.init()
    }

    override suspend fun cancelCollect(collectUserId: Long, articleId: Long): DataResponse<Unit> {
        return client.securityPost(urlString = cancelCollectPath) {
            setJsonBody(Collect(collectUserId = collectUserId, articleId = articleId))
        }.init()
    }

    override suspend fun existsCollect(
        collectUserId: Long,
        articleId: Long
    ): DataResponse<Boolean> {
        return client.post(urlString = existsCollectPath) {
            setJsonBody(Collect(collectUserId = collectUserId, articleId = articleId))
        }.init()
    }
}