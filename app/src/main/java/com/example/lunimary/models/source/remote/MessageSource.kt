package com.example.lunimary.models.source.remote

import com.example.lunimary.models.Comment
import com.example.lunimary.models.responses.CombinedCommentMessage
import com.example.lunimary.models.responses.MessageResponse

interface MessageSource {
    /**
     * need session and token.
     */
    suspend fun messageForComments(): MessageResponse<CombinedCommentMessage>
}