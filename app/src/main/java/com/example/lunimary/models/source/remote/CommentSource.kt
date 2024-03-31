package com.example.lunimary.models.source.remote

import com.example.lunimary.models.ext.CommentsWithUser
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.impl.CommentSourceImpl

interface CommentSource {

    suspend fun createComment(
        content: String,
        userId: Long,
        articleId: Long
    ): DataResponse<Unit>

    suspend fun getAllCommentsOfArticle(
        articleId: Long
    ): DataResponse<List<CommentsWithUser>>

    companion object : CommentSource by CommentSourceImpl()
}