package com.example.lunimary.ui.message

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.LikeMessage

@Composable
fun LikeMessagePage(likesMessage: LazyPagingItems<LikeMessage>) {
    LunimaryPagingContent(
        items = likesMessage,
        topItem = { Spacer(modifier = Modifier.height(16.dp)) },
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            LikeItem(it)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
