package com.example.lunimary.ui.message

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.request
import com.example.lunimary.models.Article
import com.example.lunimary.models.Comment
import com.example.lunimary.models.LikeMessage
import com.example.lunimary.models.User
import com.example.lunimary.models.ext.UserFriend
import com.example.lunimary.models.responses.CombinedCommentMessage
import com.example.lunimary.models.source.remote.repository.MessageRepository
import com.example.lunimary.network.NetworkResult

class MessageViewModel : BaseViewModel() {
    private val messageRepository = MessageRepository()

    private val _commentsMessage: MutableState<NetworkResult<CombinedCommentMessage>> =
        mutableStateOf(NetworkResult.None())
    val commentsMessage: State<NetworkResult<CombinedCommentMessage>> get() = _commentsMessage

    fun messageForComments() {
        fly(FLY_MESSAGE_FOR_COMMENTS) {
            request(
                block = {
                    _commentsMessage.value = NetworkResult.Loading()
                    messageRepository.messageForComments()
                },
                onSuccess = { data, _ ->
                    if (data?.isEmpty() == true) {
                        _commentsMessage.value = NetworkResult.Empty()
                    } else {
                        _commentsMessage.value = NetworkResult.Success(data = data)
                    }
                },
                onFailed = {
                    _commentsMessage.value = NetworkResult.Error(it)
                },
                onFinish = { land(FLY_MESSAGE_FOR_COMMENTS) }
            )
        }
    }

    fun transformCommentsData(): List<ItemData> {
        val result = mutableListOf<ItemData>()
        val data = (commentsMessage.value as? NetworkResult.Success)?.data ?: return result

        for(combined in data) {
            for (comment in combined.messages) {
                if (combined.user == null || combined.article == null) {
                    continue
                }
                result += ItemData(
                    user = combined.user,
                    comment = comment,
                    article = combined.article
                )
            }
        }
        return result.sortedByDescending { it.comment.timestamp }
    }

    private val _likesMessage: MutableState<NetworkResult<List<LikeMessage>>> =
        mutableStateOf(NetworkResult.None())
    val likesMessage: State<NetworkResult<List<LikeMessage>>> get() = _likesMessage

    fun messageForLikes() {
        fly(FLY_MESSAGE_FOR_LIKES) {
            request(
                block = {
                    _likesMessage.value = NetworkResult.Loading()
                    messageRepository.messageForLikes()
                },
                onSuccess = { data, _ ->
                    if (data?.isEmpty() == true) {
                        _likesMessage.value = NetworkResult.Empty()
                    } else {
                        _likesMessage.value = NetworkResult.Success(data = data)
                    }
                },
                onFailed = {
                    _likesMessage.value = NetworkResult.Error(it)
                },
                onFinish = { land(FLY_MESSAGE_FOR_LIKES) }
            )
        }
    }

    fun transformLikeData(): List<LikeMessage> {
        return (likesMessage.value as? NetworkResult.Success)?.data ?: return emptyList()
    }

    private val _followMessage: MutableState<NetworkResult<List<UserFriend>>> =
        mutableStateOf(NetworkResult.None())
    val followMessage: State<NetworkResult<List<UserFriend>>> get() = _followMessage

    fun messageForFollows() {
        fly(FLY_MESSAGE_FOR_FOLLOWS) {
            request(
                block = {
                    _followMessage.value = NetworkResult.Loading()
                    messageRepository.messageForFollows()
                },
                onSuccess = { data, _ ->
                    if (data?.isEmpty() == true) {
                        _followMessage.value = NetworkResult.Empty()
                    } else {
                        _followMessage.value = NetworkResult.Success(data = data)
                    }
                },
                onFailed = {
                    _followMessage.value = NetworkResult.Error(it)
                },
                onFinish = { land(FLY_MESSAGE_FOR_FOLLOWS) }
            )
        }
    }

    fun transformFollowData(): List<UserFriend> {
        return (followMessage.value as? NetworkResult.Success)?.data ?: return emptyList()
    }
}

data class ItemData(
    val user: User,
    val comment: Comment,
    val article: Article
)

const val FLY_MESSAGE_FOR_COMMENTS = "__fly_message_for_comments__"
const val FLY_MESSAGE_FOR_LIKES = "__fly_message_for_likes__"
const val FLY_MESSAGE_FOR_FOLLOWS = "__fly_message_for_follows__"
