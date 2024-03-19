package com.example.lunimary.ui.message

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessagePagers(
    pagerState: PagerState,
    tabs: List<MessagePageType>,
    messageViewModel: MessageViewModel
) {
    HorizontalPager(state = pagerState) { page ->
        when(tabs[page]) {
            MessagePageType.Comment -> {
                CommentMessagePage(messageViewModel = messageViewModel)
            }
            MessagePageType.Like -> {
                LikeMessagePage(messageViewModel = messageViewModel)
            }
            MessagePageType.Follow -> {
                FollowMessagePage(messageViewModel = messageViewModel)
            }
        }
    }
}






