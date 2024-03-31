package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.models.ext.CommentsWithUser
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.CommentSource

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
}