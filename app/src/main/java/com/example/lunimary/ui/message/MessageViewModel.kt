package com.example.lunimary.ui.message

import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.pager.pagerFlow
import com.example.lunimary.models.source.remote.paging.MessageCommentSource
import com.example.lunimary.models.source.remote.paging.MessageFollowSource
import com.example.lunimary.models.source.remote.paging.MessageLikeSource

class MessageViewModel : BaseViewModel() {

    val commentsMessage = pagerFlow { MessageCommentSource }

    val likesMessage = pagerFlow { MessageLikeSource }

    val followMessage = pagerFlow { MessageFollowSource }
}