package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.models.Comment
import com.example.lunimary.models.ktor.addPathParam
import com.example.lunimary.models.ktor.init
import com.example.lunimary.models.ktor.securityPost
import com.example.lunimary.models.ktor.setJsonBody
import com.example.lunimary.models.responses.CombinedCommentMessage
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.CommentSource
import com.example.lunimary.util.createCommentPath
import com.example.lunimary.util.getAllCommentsOfArticlePath
import io.ktor.client.request.get

class CommentSourceImpl : BaseSourceImpl by BaseSourceImpl(), CommentSource {

    override suspend fun createComment(content: String, userId: Long, articleId: Long): DataResponse<Unit> {
        return client.securityPost(urlString = createCommentPath) {
            setJsonBody(
                Comment(
                    userId = userId,
                    content = content,
                    articleId = articleId,
                    timestamp = System.currentTimeMillis()
                )
            )
        }.init()
    }

    override suspend fun getAllCommentsOfArticle(articleId: Long): DataResponse<CombinedCommentMessage> {
        return client.get(urlString = getAllCommentsOfArticlePath) {
            addPathParam(articleId)
        }.init()
    }
}