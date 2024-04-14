package com.example.lunimary.ui.message.pages

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.cascade.cascadeMenu
import com.example.lunimary.model.ext.UserFriend
import com.example.lunimary.ui.message.FollowItem
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
            item("delete", "É¾³ýÏûÏ¢") {
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
