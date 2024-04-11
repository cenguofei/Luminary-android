package com.example.lunimary.model.source.remote

import com.example.lunimary.model.LikeMessage
import com.example.lunimary.model.ext.CommentItem
import com.example.lunimary.model.ext.UserFriend
import com.example.lunimary.model.responses.PageResponse
import com.example.lunimary.model.source.remote.impl.MessageSourceImpl

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