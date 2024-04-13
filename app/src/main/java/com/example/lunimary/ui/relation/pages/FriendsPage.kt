package com.example.lunimary.ui.relation.pages

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 12.dp, end = 12.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            onClick = {}
        ) {
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                UserItem(user = item.data.user, onItemClick = onItemClick)
            }
        }
    }
}