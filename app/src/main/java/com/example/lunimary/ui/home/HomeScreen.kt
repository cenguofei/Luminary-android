package com.example.lunimary.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunimary.base.UserState
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.models.Article
import com.example.lunimary.models.User
import com.example.lunimary.ui.LunimaryAppState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit,
    onLoginClick: () -> Unit,
    appState: LunimaryAppState,
    onItemClick: (PageItem<Article>) -> Unit,
) {
    val tabs = remember {
        listOf(HomeCategories.Recommend, HomeCategories.All, HomeCategories.Following)
    }
    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }
    val userState = UserState.currentUserState.observeAsState()
    val recommendViewModel: RecommendViewModel = viewModel()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        HomeTopBar(
            tabs = tabs,
            pagerState = pagerState,
            onAddClick = onAddClick,
            onSearchClick = onSearchClick
        )

        if (userState.value == User.NONE) {
            // 1. 用户还没有登录过
            // 2. 第一次使用app
            // 3. 登录状态失效
            RemindToLogin(onLoginClick = onLoginClick)
        }

        HomeContent(
            tabs = tabs,
            modifier = Modifier.weight(1f),
            pagerState = pagerState,
            onItemClick = onItemClick,
            goToLogin = { appState.navToLogin(true) },
            recommendViewModel = recommendViewModel,
        )
    }
}
