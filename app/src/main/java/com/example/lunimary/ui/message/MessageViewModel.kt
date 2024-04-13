package com.example.lunimary.ui.message

import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.base.pager.pagerFlow
import com.example.lunimary.model.LikeMessage
import com.example.lunimary.model.ext.CommentItem
import com.example.lunimary.model.ext.UserFriend
import com.example.lunimary.model.source.remote.paging.MessageCommentSource
import com.example.lunimary.model.source.remote.paging.MessageFollowSource
import com.example.lunimary.model.source.remote.paging.MessageLikeSource
import com.example.lunimary.model.source.remote.repository.CommentRepository
import com.example.lunimary.model.source.remote.repository.FriendRepository
import com.example.lunimary.model.source.remote.repository.LikeRepository

class MessageViewModel : BaseViewModel() {
    private val commentRepository by lazy { CommentRepository() }
    private val likeRepository by lazy { LikeRepository() }
    private val friendRepository by lazy { FriendRepository() }

    /**
     * 评论分页数据
     */
    val commentsMessage = pagerFlow { MessageCommentSource }

    /**
     * 点赞分页数据
     */
    val likesMessage = pagerFlow { MessageLikeSource }

    /**
     * 被关注分页数据
     */
    val followMessage = pagerFlow { MessageFollowSource }

    /**
     * 删除评论
     */
    fun deleteComment(
        commentItem: PageItem<CommentItem>,
        onFailed: (String) -> Unit
    ) {
        fly(FLY_DELETE_COMMENT_MESSAGE) {
            request(
                block = {
                    commentRepository.deleteComment(commentItem.data.comment.id)
                },
                emptySuccess = { commentItem.onDeletedStateChange(true) },
                onFailed = { onFailed(it) },
                onFinish = { land(FLY_DELETE_COMMENT_MESSAGE) }
            )
        }
    }

    fun deleteLike(
        likeMessagePageItem: PageItem<LikeMessage>,
        onFailed: (String) -> Unit
    ) {
        fly(FLY_DELETE_LIKE_MESSAGE) {
            request(
                block = {
                    likeRepository.invisibleToUser(likeMessagePageItem.data.likeId)
                },
                emptySuccess = {
                    likeMessagePageItem.onDeletedStateChange(true)
                },
                onFailed = { onFailed(it) },
                onFinish = { land(FLY_DELETE_LIKE_MESSAGE) }
            )
        }
    }

    fun deleteFollow(
        item: PageItem<UserFriend>,
        onFailed: (String) -> Unit
    ) {
        fly(FLY_DELETE_FOLLOWING_MESSAGE) {
            request(
                block = {
                    friendRepository.invisibleFollow(item.data.user.id)
                },
                emptySuccess = {

                    item.onDeletedStateChange(true)
                },
                onSuccess = { data, msg ->
                    onFailed("$data, $msg")
                },
                onFailed = { onFailed(it) },
                onFinish = { land(FLY_DELETE_FOLLOWING_MESSAGE) }
            )
        }
    }
}

private const val FLY_DELETE_COMMENT_MESSAGE = "fly_delete_comment"
private const val FLY_DELETE_LIKE_MESSAGE = "fly_delete_like"
private const val FLY_DELETE_FOLLOWING_MESSAGE = "fly_delete_like"