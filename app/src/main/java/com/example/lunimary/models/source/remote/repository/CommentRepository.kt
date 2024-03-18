package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.BaseRepository
import com.example.lunimary.models.responses.CombinedCommentMessage
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.CommentSource
import com.example.lunimary.models.source.remote.impl.CommentSourceImpl

class CommentRepository : BaseRepository by BaseRepository(), CommentSource {
    private val source: CommentSource = CommentSourceImpl()

    override suspend fun createComment(
        content: String,
        userId: Long,
        articleId: Long
    ): DataResponse<Unit> {
        return withDispatcher { source.createComment(content, userId, articleId) }
    }

    override suspend fun getAllCommentsOfArticle(articleId: Long): DataResponse<CombinedCommentMessage> {
        return withDispatcher { source.getAllCommentsOfArticle(articleId) }
    }
}