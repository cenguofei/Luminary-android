package com.example.lunimary.ui.browse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.lunimary.base.notLogin
import com.example.lunimary.models.Article
import com.example.lunimary.models.User
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import com.example.lunimary.ui.common.PAGE_ARTICLE_ITEM_KEY
import com.example.lunimary.ui.common.PageArticleNavArguments
import com.example.lunimary.ui.edit.EditType
import com.example.lunimary.util.empty
import com.example.lunimary.util.logd

fun NavGraphBuilder.browseScreen(
    appState: LunimaryAppState
) {
    composable(
        route = Screens.BrowseArticle.route
    ) {
        if (notLogin()) {
            LaunchedEffect(
                key1 = Unit,
                block = {
                    appState.navToLogin()
                }
            )
        }
        val article = PageArticleNavArguments[PAGE_ARTICLE_ITEM_KEY]
        if (article == null) {
            appState.navToHome(Screens.BrowseArticle.route)
            "nav article = null".logd()
            return@composable
        }
        val browseViewModel: BrowseViewModel = viewModel()
        browseViewModel.setArticle(article.data)
        BrowseScreen(
            onBack = appState::popBackStack,
            browseViewModel = browseViewModel,
            onLinkClick = appState::navToWeb,
            onUserClick = { appState.navToViewUser(it, Screens.BrowseArticle.route) },
            onArticleDeleted = {
                article.deleted.value = true
                appState.popBackStack()
            },
            navToEdit = appState::navToEdit
        )
        DisposableEffect(
            key1 = Unit,
            effect = {
                browseViewModel.beginRecord()
                onDispose {
                    browseViewModel.endRecord()
                }
            }
        )
    }
}

@Composable
fun BrowseScreen(
    onBack: () -> Unit,
    browseViewModel: BrowseViewModel,
    onLinkClick: (String) -> Unit,
    onUserClick: (User) -> Unit,
    onArticleDeleted: (Article) -> Unit,
    navToEdit: (EditType, Article) -> Unit
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
            navToEdit = navToEdit
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





