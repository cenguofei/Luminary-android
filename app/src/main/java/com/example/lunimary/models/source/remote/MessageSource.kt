package com.example.lunimary.models.source.remote

import com.example.lunimary.models.LikeMessageResponse
import com.example.lunimary.models.ext.UserFriend
import com.example.lunimary.models.responses.CombinedCommentMessage
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.MessageResponse

interface MessageSource {
    /**
     * need session and token.
     */
    suspend fun messageForComments(): MessageResponse<CombinedCommentMessage>

    suspend fun messageForLikes(): LikeMessageResponse

    suspend fun messageForFollows(): DataResponse<List<UserFriend>>
}