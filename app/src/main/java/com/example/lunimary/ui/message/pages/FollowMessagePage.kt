package com.example.lunimary.ui.message.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.cascade.cascadeMenu
import com.example.lunimary.design.nicepage.LunimaryPagingContent
import com.example.lunimary.model.ext.UserFriend
import com.example.lunimary.ui.message.FollowItem
import com.example.lunimary.ui.message.LikeItem
import com.example.lunimary.ui.message.MessageViewModel

@Composable
fun FollowMessagePage(
    followMessage: LazyPagingItems<PageItem<UserFriend>>,
    messageViewModel: MessageViewModel,
    onShowSnackbar: (msg: String, label: String?) -> Unit
) {
    MessagePage(
        items = followMessage,
        menu = cascadeMenu {
            item("delete", "É¾³ý¹Ø×¢") {
                icon(Icons.Default.Delete)
            }
        },
        onItemSelected = { id, item ->
            if (id == "delete") {
                messageViewModel.deleteFollow(item) {
                    onShowSnackbar(it, null)
                }
            }
        }
    ) {
        FollowItem(item = it)
    }
}
