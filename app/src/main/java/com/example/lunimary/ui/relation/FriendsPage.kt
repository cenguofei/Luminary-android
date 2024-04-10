package com.example.lunimary.ui.relation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.nicepage.LunimaryPagingContent
import com.example.lunimary.models.User
import com.example.lunimary.models.ext.UserFriend
import com.example.lunimary.ui.common.UserItem
import com.example.lunimary.util.logi

@Composable
fun FriendsPage(
    onItemClick: (User) -> Unit,
    friends: LazyPagingItems<PageItem<UserFriend>>
) {
    LaunchedEffect(
        key1 = Unit,
        block = {
            friends.refresh()
            "friends.refresh".logi("relation_refresh")
        }
    )
    LunimaryPagingContent(
        items = friends,
        topItem = { Spacer(modifier = Modifier.height(16.dp)) }
    ) { _, item ->
        Column {
            UserItem(user = item.data.user, onItemClick = onItemClick)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}