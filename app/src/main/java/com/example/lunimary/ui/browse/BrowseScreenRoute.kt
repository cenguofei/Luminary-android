package com.example.lunimary.ui.browse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.lunimary.model.Article
import com.example.lunimary.model.User
import com.example.lunimary.ui.browse.compositions.BrowseScreenContent
import com.example.lunimary.ui.browse.compositions.EditComment
import com.example.lunimary.ui.edit.EditType
import com.example.lunimary.util.empty

@Composable
internal fun BrowseScreenRoute(
    onBack: () -> Unit,
    browseViewModel: BrowseViewModel,
    onLinkClick: (String) -> Unit,
    onUserClick: (User) -> Unit,
    onArticleDeleted: (Article) -> Unit,
    navToEdit: (EditType, Article) -> Unit,
    onShowSnackbar: (msg: String, label: String?) -> Unit
) {
    val showEditContent = remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        BrowseScreenContent(
            onBack = onBack,
            browseViewModel = browseViewModel,
            onFollowClick = browseViewModel::onFollowClick,
            onUnfollowClick = browseViewModel::onUnfollowClick,
            onEditCommentClick = { showEditContent.value = true },
            onLinkClick = onLinkClick,
            onUserClick = onUserClick,
            onArticleDeleted = onArticleDeleted,
            navToEdit = navToEdit,
            onShowSnackbar = onShowSnackbar
        )
        val commentText = remember { mutableStateOf(empty) }
        if (showEditContent.value) {
            EditComment(
                commentText = commentText,
                onDismiss = { showEditContent.value = false },
                onSend = {
                    showEditContent.value = false
                    browseViewModel.send(it)
                }
            )
        }
    }
}