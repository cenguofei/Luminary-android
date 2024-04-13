package com.example.lunimary.ui.message.pages

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.cascade.cascadeMenu
import com.example.lunimary.model.LikeMessage
import com.example.lunimary.ui.message.LikeItem
import com.example.lunimary.ui.message.MessageViewModel

@Composable
fun LikeMessagePage(
    likesMessage: LazyPagingItems<PageItem<LikeMessage>>,
    messageViewModel: MessageViewModel,
    onShowSnackbar: (msg: String, label: String?) -> Unit
) {
    MessagePage(
        items = likesMessage,
        menu = cascadeMenu {
            item("delete", "É¾³ýµãÔÞ") {
                icon(Icons.Default.Delete)
            }
        },
        onItemSelected = { id, item ->
            if (id == "delete") {
                messageViewModel.deleteLike(item) {
                    onShowSnackbar(it, null)
                }
            }
        }
    ) {
        LikeItem(it)
    }
}
