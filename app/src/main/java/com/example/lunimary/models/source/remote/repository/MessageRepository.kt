package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.BaseRepository
import com.example.lunimary.models.responses.CombinedCommentMessage
import com.example.lunimary.models.responses.MessageResponse
import com.example.lunimary.models.source.remote.MessageSource
import com.example.lunimary.models.source.remote.impl.MessageSourceImpl

class MessageRepository : BaseRepository by BaseRepository(), MessageSource {
    private val source: MessageSource = MessageSourceImpl()

    override suspend fun messageForComments(): MessageResponse<CombinedCommentMessage> {
        return withDispatcher { source.messageForComments() }
    }
}