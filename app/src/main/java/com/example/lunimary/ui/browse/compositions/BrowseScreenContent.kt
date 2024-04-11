package com.example.lunimary.ui.browse.compositions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lunimary.model.Article
import com.example.lunimary.model.User
import com.example.lunimary.model.checkUserNotNone
import com.example.lunimary.ui.browse.BrowseViewModel
import com.example.lunimary.ui.browse.UiState
import com.example.lunimary.ui.edit.EditType

@Composable
fun BrowseScreenContent(
    onBack: () -> Unit,
    onFollowClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    browseViewModel: BrowseViewModel,
    onEditCommentClick: () -> Unit,
    onLinkClick: (String) -> Unit,
    onUserClick: (User) -> Unit,
    navToEdit: (EditType, Article) -> Unit,
    onShowSnackbar: (msg: String, label: String?) -> Unit,
    uiState: UiState
) {
    val article = uiState.article
    val listState = rememberLazyListState()
    val commentsSize = remember { mutableStateOf(0) }
    Column(modifier = Modifier.fillMaxSize()) {
        DynamicToolBar(
            onBack = onBack,
            onFollowClick = onFollowClick,
            onUnfollowClick = onUnfollowClick,
            listState = listState,
            uiState = uiState,
            browseViewModel = browseViewModel,
            onUserClick = onUserClick,
            navToEdit = navToEdit,
            onShowSnackbar = onShowSnackbar
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = listState
        ) {
            if (article.isLunimaryStation) {
                item {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)) {
                        ArticleTitle(title = uiState.article.title)
                    }
                }
            }
            if (article.isLunimaryStation) {
                item {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)) {
                        ArticleOwner(
                            uiState = uiState,
                            onUnfollowClick = onUnfollowClick,
                            onFollowClick = onFollowClick,
                            browseViewModel = browseViewModel,
                            onUserClick = {
                                checkUserNotNone {
                                    onUserClick(browseViewModel.articleOwner.value)
                                }
                            }
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    BodyContent(
                        article = article,
                        onBack = onBack,
                        commentsSize = commentsSize,
                        browseViewModel = browseViewModel
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        InteractionBottomBar(
            browseViewModel = browseViewModel,
            modifier = Modifier.padding(horizontal = 16.dp),
            onEditCommentClick = onEditCommentClick
        )
    }
}