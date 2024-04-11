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
import com.example.lunimary.model.LikeMessage
import com.example.lunimary.ui.message.LikeItem

@Composable
fun LikeMessagePage(likesMessage: LazyPagingItems<PageItem<LikeMessage>>) {
    LunimaryPagingContent(
        items = likesMessage,
        topItem = { Spacer(modifier = Modifier.height(16.dp)) },
    ) { _, item ->
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            LikeItem(item.data)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
