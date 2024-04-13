package com.example.lunimary.model.source.remote.impl

import com.example.lunimary.base.ktor.addPathParam
import com.example.lunimary.base.ktor.init
import com.example.lunimary.base.ktor.securityDelete
import com.example.lunimary.base.ktor.securityPost
import com.example.lunimary.base.ktor.setJsonBody
import com.example.lunimary.model.Like
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.LikeSource
import com.example.lunimary.util.cancelLikePath
import com.example.lunimary.util.createLikePath
import com.example.lunimary.util.deleteLikePath
import com.example.lunimary.util.existsLikePath
import io.ktor.client.request.post

class LikeSourceImpl : BaseSourceImpl by BaseSourceImpl(), LikeSource {

    override suspend fun giveLike(userId: Long, articleId: Long): DataResponse<Unit> {
        return client.securityPost(urlString = createLikePath) {
            setJsonBody(Like(userId = userId, articleId = articleId, timestamp = System.currentTimeMillis()))
        }.init()
    }

    override suspend fun cancelLike(userId: Long, articleId: Long): DataResponse<Unit> {
        return client.securityPost(urlString = cancelLikePath) {
            setJsonBody(Like(userId = userId, articleId = articleId))
        }.init()
    }

    override suspend fun existsLike(userId: Long, articleId: Long): DataResponse<Boolean> {
        return client.post(urlString = existsLikePath) {
            setJsonBody(Like(userId = userId, articleId = articleId))
        }.init()
    }

    override suspend fun invisibleToUser(likeId: Long): DataResponse<Boolean> {
        return client.securityDelete(deleteLikePath) {
            addPathParam(likeId)
        }.init()
    }
}