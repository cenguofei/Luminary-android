package com.example.lunimary.ui.message

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.ui.message.pages.CommentMessagePage
import com.example.lunimary.ui.message.pages.FollowMessagePage
import com.example.lunimary.ui.message.pages.LikeMessagePage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessagePagers(
    pagerState: PagerState,
    tabs: List<MessagePageType>,
    messageViewModel: MessageViewModel,
    onShowSnackbar: (msg: String, label: String?) -> Unit,
) {
    val commentsMessage = messageViewModel.commentsMessage.collectAsLazyPagingItems()
    val likesMessage = messageViewModel.likesMessage.collectAsLazyPagingItems()
    val followMessage = messageViewModel.followMessage.collectAsLazyPagingItems()

    DisposableEffect(
        key1 = Unit,
        effect = {
            messageViewModel.registerOnHaveNetwork("CommentMessagePage") {
                commentsMessage.retry()
            }
            messageViewModel.registerOnHaveNetwork("LikeMessagePage") {
                likesMessage.retry()
            }
            messageViewModel.registerOnHaveNetwork("FollowMessagePage") {
                followMessage.retry()
            }
            onDispose {
                messageViewModel.unregisterOnHaveNetwork("CommentMessagePage")
                messageViewModel.unregisterOnHaveNetwork("LikeMessagePage")
                messageViewModel.unregisterOnHaveNetwork("FollowMessagePage")
            }
        }
    )
    HorizontalPager(state = pagerState) { page ->
        when (tabs[page]) {
            MessagePageType.Comment -> {
                CommentMessagePage(
                    commentsMessage = commentsMessage,
                    messageViewModel = messageViewModel,
                    onShowSnackbar = onShowSnackbar
                )
            }

            MessagePageType.Like -> {
                LikeMessagePage(
                    likesMessage = likesMessage,
                    messageViewModel = messageViewModel,
                    onShowSnackbar = onShowSnackbar
                )
            }

            MessagePageType.Follow -> {
                FollowMessagePage(
                    followMessage = followMessage,
                    messageViewModel = messageViewModel,
                    onShowSnackbar = onShowSnackbar
                )
            }
        }
    }
}






