package com.example.lunimary.ui.message

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.lunimary.design.LunimaryScreen
import com.example.lunimary.models.Comment
import com.example.lunimary.models.responses.CombinedMessage
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.network.isCurrentlyConnected

@Composable
fun CommentMessagePage(messageViewModel: MessageViewModel) {
    LaunchedEffect(
        key1 = Unit,
        block = { messageViewModel.messageForComments() }
    )
    val networkResult = messageViewModel.commentsMessage.value
    LunimaryScreen(
        networkResult = networkResult,
        networkError = !LocalContext.current.isCurrentlyConnected()
    ) {
        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
            when(networkResult) {
                is NetworkResult.Success -> {
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    items(messageViewModel.transformData()) { item: ItemData ->
                        CommentItem(
                            user = item.user,
                            article = item.article,
                            comment = item.comment
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                else -> {}
            }
        }
    }
}