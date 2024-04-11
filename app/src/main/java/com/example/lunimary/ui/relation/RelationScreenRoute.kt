package com.example.lunimary.ui.relation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.design.components.BackButton
import com.example.lunimary.model.User
import com.example.lunimary.ui.common.RelationPageType
import com.example.lunimary.ui.relation.pages.FollowPage
import com.example.lunimary.ui.relation.pages.FollowersPage
import com.example.lunimary.ui.relation.pages.FriendsPage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RelationScreenRoute(
    pageType: RelationPageType,
    onBack: () -> Unit,
    relationViewModel: RelationViewModel,
    onItemClick: (User) -> Unit
) {
    val tabs = remember {
        listOf(
            RelationPageType.Friends, RelationPageType.Follow, RelationPageType.Followers
        )
    }
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = pageType.ordinal) { tabs.size }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
        ) {
            TopTabBar(
                pagerState = pagerState,
                coroutineScope = coroutineScope,
                tabs = tabs
            )
            BackButton(
                modifier = Modifier
                    .padding(start = 12.dp),
                onClick = onBack,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        val friends = relationViewModel.friends.collectAsLazyPagingItems()
        val followings = relationViewModel.followings.collectAsLazyPagingItems()
        val followers = relationViewModel.followers.collectAsLazyPagingItems()

        DisposableEffect(
            key1 = Unit,
            effect = {
                relationViewModel.registerOnHaveNetwork("FriendsPage") {
                    friends.retry()
                }
                relationViewModel.registerOnHaveNetwork("FollowPage") {
                    followings.retry()
                }
                relationViewModel.registerOnHaveNetwork("FollowersPage") {
                    followers.retry()
                }
                onDispose {
                    relationViewModel.unregisterOnHaveNetwork("FriendsPage")
                    relationViewModel.unregisterOnHaveNetwork("FollowPage")
                    relationViewModel.unregisterOnHaveNetwork("FollowersPage")
                }
            }
        )
        HorizontalPager(state = pagerState) {
            when (tabs[it]) {
                RelationPageType.Friends -> {
                    FriendsPage(
                        onItemClick = onItemClick,
                        friends = friends
                    )
                }

                RelationPageType.Follow -> {
                    FollowPage(
                        onItemClick = onItemClick,
                        followings = followings,
                        relationViewModel = relationViewModel
                    )
                }

                RelationPageType.Followers -> {
                    FollowersPage(
                        followers = followers,
                        onItemClick = onItemClick,
                        relationViewModel = relationViewModel
                    )
                }
            }
        }
    }
}