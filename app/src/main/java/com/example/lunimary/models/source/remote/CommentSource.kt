package com.example.lunimary.models.source.remote

import com.example.lunimary.models.responses.CombinedCommentMessage
import com.example.lunimary.models.responses.DataResponse

interface CommentSource {

    suspend fun createComment(
        content: String,
        userId: Long,
        articleId: Long
    ): DataResponse<Unit>

    suspend fun getAllCommentsOfArticle(
        articleId: Long
    ): DataResponse<CombinedCommentMessage>
}