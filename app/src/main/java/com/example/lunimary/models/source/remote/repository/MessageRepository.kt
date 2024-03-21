package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.BaseRepository
import com.example.lunimary.models.LikeMessageResponse
import com.example.lunimary.models.ext.UserFriend
import com.example.lunimary.models.responses.CombinedCommentMessage
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.MessageResponse
import com.example.lunimary.models.source.remote.MessageSource
import com.example.lunimary.models.source.remote.impl.MessageSourceImpl

class MessageRepository : BaseRepository by BaseRepository(), MessageSource {
    private val source: MessageSource = MessageSourceImpl()

    override suspend fun messageForComments(): MessageResponse<CombinedCommentMessage> {
        return withDispatcher { source.messageForComments() }
    }

    override suspend fun messageForLikes(): LikeMessageResponse {
        return withDispatcher { source.messageForLikes() }
    }

    override suspend fun messageForFollows(): DataResponse<List<UserFriend>> {
        return withDispatcher { source.messageForFollows() }
    }
}