package com.example.lunimary.ui.browse

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.model.Article
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import com.example.lunimary.ui.common.NotLoginEffect
import com.example.lunimary.ui.common.PAGE_ARTICLE_ITEM_KEY
import com.example.lunimary.ui.common.PageArticleNavArguments
import com.example.lunimary.util.logd

fun NavGraphBuilder.browseScreen(
    appState: LunimaryAppState,
    onShowSnackbar: (msg: String, label: String?) -> Unit
) {
    composable(
        route = Screens.BrowseArticle.route
    ) {
        NotLoginEffect(appState = appState)
        val articleItem = PageArticleNavArguments[PAGE_ARTICLE_ITEM_KEY]
        ArticleEffect(articleItem = articleItem, appState = appState)
        val browseViewModel: BrowseViewModel = viewModel()
        val uiState = browseViewModel.uiState.collectAsStateWithLifecycle()
        if(uiState.value.articleDeleted) {
            LaunchedEffect(
                key1 = Unit,
                block = {
                    articleItem?.onDeletedStateChange(true)
                    appState.popBackStack()
                }
            )
        }
        articleItem as PageItem<Article>
        SetArticleEffect(articleItem = articleItem, browseViewModel = browseViewModel)
        BrowseScreenRoute(
            onBack = appState::popBackStack,
            browseViewModel = browseViewModel,
            onLinkClick = appState::navToWeb,
            onUserClick = { appState.navToViewUser(it, Screens.BrowseArticle.route) },
            navToEdit = { type, _ ->
                appState.navToEdit(type, articleItem)
            },
            onShowSnackbar = onShowSnackbar,
            uiState = uiState.value
        )
        RecordEffect(browseViewModel = browseViewModel)
    }
}

@Composable
private fun RecordEffect(browseViewModel: BrowseViewModel) {
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

@Composable
private fun SetArticleEffect(
    articleItem: PageItem<Article>,
    browseViewModel: BrowseViewModel
) {
    LaunchedEffect(
        key1 = articleItem,
        block = { browseViewModel.setArticle(articleItem.data) }
    )
}

@Composable
private fun ArticleEffect(articleItem: PageItem<Article>?, appState: LunimaryAppState) {
    if(articleItem == null) {
        LaunchedEffect(
            key1 = Unit,
            block = {
                appState.navToHome(Screens.BrowseArticle.route)
                "nav article = null".logd()
            }
        )
    }
}



