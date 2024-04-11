package com.example.lunimary.ui.message.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.nicepage.LunimaryPagingContent
import com.example.lunimary.model.ext.UserFriend
import com.example.lunimary.ui.message.FollowItem
import java.util.UUID

@Composable
fun FollowMessagePage(followMessage: LazyPagingItems<PageItem<UserFriend>>) {
    LunimaryPagingContent(
        items = followMessage,
        key = { followMessage[it]?.data?.user?.id ?: UUID.randomUUID() },
        topItem = { Spacer(modifier = Modifier.height(16.dp)) },
    ) { _, item ->
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            FollowItem(item = item.data)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
