package com.example.lunimary.models.responses

import com.example.lunimary.base.BaseResponse
import com.example.lunimary.models.Article
import com.example.lunimary.models.Comment
import com.example.lunimary.models.Friend
import com.example.lunimary.models.Like
import com.example.lunimary.models.User

/**
 * @param data list of [CombinedMessage]
 */
@kotlinx.serialization.Serializable
class MessageResponse<L> : BaseResponse<L>()

/**
 * @param message type of [Like], [Comment], [Friend]
 */
@kotlinx.serialization.Serializable
data class CombinedMessage<T>(
    val user: User? = null,
    val article: Article? = null,
    val messages: List<T> = emptyList()
)


typealias CombinedCommentMessage = List<CombinedMessage<Comment>>
typealias CombinedLikeMessage = List<CombinedMessage<Like>>
typealias CombinedFollowMessage = List<CombinedMessage<Friend>>