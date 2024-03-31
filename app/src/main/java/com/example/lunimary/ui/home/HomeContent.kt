package com.example.lunimary.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
    HorizontalPager(state = pagerState, modifier = modifier) {
        when {
            tabs[it] == HomeCategories.Recommend -> {
                RecommendPage(
                    recommendViewModel = recommendViewModel,
                    onItemClick = onItemClick
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
                            recommendViewModel = recommendViewModel,
                            onItemClick = onItemClick
                        )
                    }
                }
            }
        }
    }
}