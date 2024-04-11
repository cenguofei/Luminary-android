package com.example.lunimary.model.source.remote.impl

import com.example.lunimary.base.ktor.addPagesParam
import com.example.lunimary.base.ktor.init
import com.example.lunimary.base.ktor.securityGet
import com.example.lunimary.model.LikeMessage
import com.example.lunimary.model.ext.CommentItem
import com.example.lunimary.model.ext.UserFriend
import com.example.lunimary.model.responses.PageResponse
import com.example.lunimary.model.source.remote.MessageSource
import com.example.lunimary.util.messageCommentPath
import com.example.lunimary.util.messageFollowPath
import com.example.lunimary.util.messageLikePath

class MessageSourceImpl : BaseSourceImpl by BaseSourceImpl(), MessageSource {
    override suspend fun messageForComments(
        curPage: Int,
        perPageCount: Int
    ): PageResponse<CommentItem> {
        return client.securityGet(
            urlString = messageCommentPath,
            block = { addPagesParam(curPage, perPageCount) }
        ).init()
    }

    override suspend fun messageForLikes(
        curPage: Int,
        perPageCount: Int
    ): PageResponse<LikeMessage> {
        return client.securityGet(
            urlString = messageLikePath,
            block = { addPagesParam(curPage, perPageCount) }
        ).init()
    }

    override suspend fun messageForFollows(
        curPage: Int,
        perPageCount: Int
    ): PageResponse<UserFriend> {
        return client.securityGet(
            urlString = messageFollowPath,
            block = { addPagesParam(curPage, perPageCount) }
        ).init()
    }
}