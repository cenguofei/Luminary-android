package com.example.lunimary.ui.message.pages

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.cascade.cascadeMenu
import com.example.lunimary.model.ext.CommentItem
import com.example.lunimary.ui.message.MessageCommentItem
import com.example.lunimary.ui.message.MessageViewModel

@Composable
fun CommentMessagePage(
    commentsMessage: LazyPagingItems<PageItem<CommentItem>>,
    messageViewModel: MessageViewModel,
    onShowSnackbar: (msg: String, label: String?) -> Unit,
) {
    MessagePage(
        items = commentsMessage,
        menu = cascadeMenu {
            item("delete", "É¾³ýÏûÏ¢") {
                icon(Icons.Default.Delete)
            }
        },
        onItemSelected = { id, item ->
            if (id == "delete") {
                messageViewModel.deleteComment(item) {
                    onShowSnackbar(it, null)
                }
            }
        },
        key = { commentsMessage[it]?.data?.comment?.id!! }
    ) {
        MessageCommentItem(
            user = it.user,
            article = it.article,
            comment = it.comment,
            modifier = Modifier
        )
    }
}
