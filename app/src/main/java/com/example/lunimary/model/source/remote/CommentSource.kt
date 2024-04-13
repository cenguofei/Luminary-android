package com.example.lunimary.model.source.remote

import com.example.lunimary.model.Comment
import com.example.lunimary.model.ext.CommentsWithUser
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.impl.CommentSourceImpl

interface CommentSource {

    suspend fun createComment(
        content: String,
        userId: Long,
        articleId: Long
    ): DataResponse<Unit>

    suspend fun getAllCommentsOfArticle(
        articleId: Long
    ): DataResponse<List<CommentsWithUser>>

    suspend fun deleteComment(
        commentId: Long
    ): DataResponse<Boolean>

    companion object : CommentSource by CommentSourceImpl()
}