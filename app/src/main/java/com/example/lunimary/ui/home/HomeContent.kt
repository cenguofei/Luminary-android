package com.example.lunimary.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunimary.util.logd
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    tabs: List<HomeCategories>,
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
) {
    val recommendViewModel: RecommendViewModel = viewModel()
    HorizontalPager(state = pagerState, modifier = modifier) {
        when(tabs[it]) {
            HomeCategories.Recommend -> {
                RecommendPage(
                    recommendViewModel = recommendViewModel,
                    coroutineScope = coroutineScope
                )
            }
            HomeCategories.Following -> {
                FollowingPage()
            }
        }
    }
}