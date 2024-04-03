package com.example.lunimary.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.base.notLogin
import com.example.lunimary.models.Article
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    tabs: List<HomeCategories>,
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    onItemClick: (Article) -> Unit,
    goToLogin: () -> Unit,
    recommendViewModel: RecommendViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val articles = recommendViewModel.recommendArticles.collectAsLazyPagingItems()
    val friendsArticles = recommendViewModel.friendsArticles.collectAsLazyPagingItems()
    DisposableEffect(key1 = Unit) {
        recommendViewModel.registerOnHaveNetwork("RecommendPage") { articles.retry() }
        recommendViewModel.registerOnHaveNetwork("FriendsArticlePage") { friendsArticles.retry() }
        onDispose {
            recommendViewModel.unregisterOnHaveNetwork("RecommendPage")
            recommendViewModel.unregisterOnHaveNetwork("FriendsArticlePage")
        }
    }
    HorizontalPager(state = pagerState, modifier = modifier) {
        when {
            tabs[it] == HomeCategories.Recommend -> {
                RecommendPage(
                    onItemClick = onItemClick,
                    articles = articles
                )
            }
            tabs[it] == HomeCategories.Following -> {
                if (recommendViewModel.goToLogin) {
                    coroutineScope.launch {
                        pagerState.scrollToPage(0)
                    }
                    recommendViewModel.updateGoToLogin(false)
                } else {
                    if (notLogin()) {
                        goToLogin()
                        recommendViewModel.updateGoToLogin(true)
                    } else {
                        FriendsArticlePage(
                            onItemClick = onItemClick,
                            friendsArticles = friendsArticles
                        )
                    }
                }
            }
        }
    }
}