package com.example.lunimary.models.source.remote

import com.example.lunimary.models.LikeMessage
import com.example.lunimary.models.ext.CommentItem
import com.example.lunimary.models.ext.UserFriend
import com.example.lunimary.models.responses.PageResponse
import com.example.lunimary.models.source.remote.impl.MessageSourceImpl

interface MessageSource {
    /**
     * need session and token.
     */
    suspend fun messageForComments(
        curPage: Int,
        perPageCount: Int
    ): PageResponse<CommentItem>

    suspend fun messageForLikes(
        curPage: Int,
        perPageCount: Int
    ): PageResponse<LikeMessage>

    suspend fun messageForFollows(
        curPage: Int,
        perPageCount: Int
    ): PageResponse<UserFriend>

    companion object : MessageSource by MessageSourceImpl()
}