package com.example.lunimary.ui.message

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.request
import com.example.lunimary.models.Article
import com.example.lunimary.models.Comment
import com.example.lunimary.models.User
import com.example.lunimary.models.responses.CombinedCommentMessage
import com.example.lunimary.models.source.remote.repository.MessageRepository
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.util.isNull

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

    fun transformData(): List<ItemData> {
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
}

data class ItemData(
    val user: User,
    val comment: Comment,
    val article: Article
)

const val FLY_MESSAGE_FOR_COMMENTS = "fly_message_for_comments"
