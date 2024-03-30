package com.example.lunimary.ui.message

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import java.util.UUID

@Composable
fun FollowMessagePage(messageViewModel: MessageViewModel) {
    val followMessage = messageViewModel.followMessage.collectAsLazyPagingItems()
    LunimaryPagingContent(
        items = followMessage,
        key = { followMessage[it]?.user?.id ?: UUID.randomUUID() },
        topItem = { Spacer(modifier = Modifier.height(16.dp)) },
        viewModel = messageViewModel,
        pagingKey = "FollowMessagePage"
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            FollowItem(item = it)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
