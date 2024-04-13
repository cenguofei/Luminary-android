package com.example.lunimary.model.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.model.ext.CommentsWithUser
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.CommentSource

class CommentRepository : Repository by Repository() {
    private val source = CommentSource

    suspend fun createComment(
        content: String,
        userId: Long,
        articleId: Long
    ): DataResponse<Unit> {
        return withDispatcher { source.createComment(content, userId, articleId) }
    }

    suspend fun getAllCommentsOfArticle(articleId: Long): DataResponse<List<CommentsWithUser>> {
        return withDispatcher { source.getAllCommentsOfArticle(articleId) }
    }

    suspend fun deleteComment(commentId: Long): DataResponse<Boolean> =
        withDispatcher { source.deleteComment(commentId) }
}