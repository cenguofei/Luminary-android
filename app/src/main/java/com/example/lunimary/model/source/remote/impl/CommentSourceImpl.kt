package com.example.lunimary.model.source.remote.impl

import com.example.lunimary.base.ktor.addPathParam
import com.example.lunimary.base.ktor.init
import com.example.lunimary.base.ktor.securityDelete
import com.example.lunimary.base.ktor.securityPost
import com.example.lunimary.base.ktor.setJsonBody
import com.example.lunimary.model.Comment
import com.example.lunimary.model.ext.CommentsWithUser
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.CommentSource
import com.example.lunimary.util.createCommentPath
import com.example.lunimary.util.deleteCommentPath
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

    override suspend fun getAllCommentsOfArticle(articleId: Long): DataResponse<List<CommentsWithUser>> {
        return client.get(urlString = getAllCommentsOfArticlePath) {
            addPathParam(articleId)
        }.init()
    }

    override suspend fun deleteComment(commentId: Long): DataResponse<Boolean> {
        return client.securityDelete(deleteCommentPath) {
            addPathParam(commentId)
        }.init()
    }
}