package com.example.lunimary.ui.relation.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.nicepage.LunimaryPagingContent
import com.example.lunimary.model.User
import com.example.lunimary.model.ext.UserFriend
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
        items = friends
    ) { _, item ->
        UserItem(user = item.data.user, onItemClick = onItemClick)
    }
}