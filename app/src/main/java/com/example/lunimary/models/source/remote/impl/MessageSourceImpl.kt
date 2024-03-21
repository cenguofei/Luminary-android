package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.models.LikeMessageResponse
import com.example.lunimary.models.User
import com.example.lunimary.models.ext.UserFriend
import com.example.lunimary.models.ktor.init
import com.example.lunimary.models.ktor.securityGet
import com.example.lunimary.models.responses.CombinedCommentMessage
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.MessageResponse
import com.example.lunimary.models.source.remote.MessageSource
import com.example.lunimary.util.messageCommentPath
import com.example.lunimary.util.messageFollowPath
import com.example.lunimary.util.messageLikePath

class MessageSourceImpl : BaseSourceImpl by BaseSourceImpl(), MessageSource {
    override suspend fun messageForComments(): MessageResponse<CombinedCommentMessage> {
        return client.securityGet(urlString = messageCommentPath).init()
    }

    override suspend fun messageForLikes(): LikeMessageResponse {
        return client.securityGet(urlString = messageLikePath).init()
    }

    override suspend fun messageForFollows(): DataResponse<List<UserFriend>> {
        return client.securityGet(urlString = messageFollowPath).init()
    }
}