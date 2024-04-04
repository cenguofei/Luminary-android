package com.example.lunimary.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.base.notLogin
import com.example.lunimary.models.Article
import com.example.lunimary.util.logi
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
    val allArticles = recommendViewModel.allArticles.collectAsLazyPagingItems()
    val recommendArticles = recommendViewModel.recommendArticles.collectAsLazyPagingItems()
    val friendsArticles = recommendViewModel.friendsArticles.collectAsLazyPagingItems()
    DisposableEffect(key1 = Unit) {
        recommendViewModel.registerOnHaveNetwork("AllPage") { allArticles.retry() }
        recommendViewModel.registerOnHaveNetwork("FriendsArticlePage") { friendsArticles.retry() }
        recommendViewModel.registerOnHaveNetwork("RecommendPage") { recommendArticles.retry() }
        onDispose {
            recommendViewModel.unregisterOnHaveNetwork("RecommendPage")
            recommendViewModel.unregisterOnHaveNetwork("AllPage")
            recommendViewModel.unregisterOnHaveNetwork("FriendsArticlePage")
        }
    }
    if (!pagerState.isScrollInProgress &&
        tabs[pagerState.currentPage] == HomeCategories.Following && notLogin()) {
        LaunchedEffect(
            key1 = pagerState.currentPage,
            block = {
                coroutineScope.launch {
                    pagerState.scrollToPage(page = 1)
                }
                "HorizontalPager LaunchedEffect goToLogin".logi("goto_login")
                goToLogin()
            }
        )
    }
    HorizontalPager(state = pagerState, modifier = modifier) {
        when {
            tabs[it] == HomeCategories.All -> {
                AllPage(
                    onItemClick = onItemClick,
                    articles = allArticles
                )
            }

            tabs[it] == HomeCategories.Recommend -> {
                RecommendPage(
                    onItemClick = onItemClick,
                    articles = recommendArticles
                )
            }

            tabs[it] == HomeCategories.Following -> {
                if (!notLogin()) {
                    FriendsArticlePage(
                        onItemClick = onItemClick,
                        friendsArticles = friendsArticles
                    )
                }
            }
        }
    }
}