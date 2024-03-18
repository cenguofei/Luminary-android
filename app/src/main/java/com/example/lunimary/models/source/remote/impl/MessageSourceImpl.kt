package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.models.ktor.init
import com.example.lunimary.models.ktor.securityGet
import com.example.lunimary.models.responses.CombinedCommentMessage
import com.example.lunimary.models.responses.MessageResponse
import com.example.lunimary.models.source.remote.MessageSource
import com.example.lunimary.util.messageCommentPath
import io.ktor.client.request.get

class MessageSourceImpl : BaseSourceImpl by BaseSourceImpl(), MessageSource {
    override suspend fun messageForComments(): MessageResponse<CombinedCommentMessage> {
        return client.securityGet(urlString = messageCommentPath).init()
    }
}