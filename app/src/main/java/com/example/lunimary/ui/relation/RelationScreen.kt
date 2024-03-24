package com.example.lunimary.ui.relation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.design.LunimaryToolbar
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import com.example.lunimary.ui.common.RelationPageType
import com.example.lunimary.ui.common.getRelationPage

fun NavGraphBuilder.relationScreen(appState: LunimaryAppState) {
    composable(
        route = Screens.Relation.route
    ) {
        val pageType = getRelationPage()
        val relationViewModel: RelationViewModel = viewModel()
        RelationScreen(
            pageType = pageType,
            onBack = appState::popBackStack,
            relationViewModel = relationViewModel
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RelationScreen(
    pageType: RelationPageType,
    onBack: () -> Unit,
    relationViewModel: RelationViewModel,
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
            modifier = Modifier.fillMaxWidth().statusBarsPadding(),
        ) {
            TopTabBar(
                pagerState = pagerState,
                coroutineScope = coroutineScope,
                tabs = tabs
            )
            IconButton(
                modifier = Modifier
                    .padding(start = 12.dp),
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        HorizontalPager(state = pagerState) {
            when (tabs[it]) {
                RelationPageType.Friends -> {
                    FriendsPage(
                        relationViewModel = relationViewModel,
                    )
                }

                RelationPageType.Follow -> {
                    FollowPage(
                        relationViewModel = relationViewModel,
                    )
                }

                RelationPageType.Followers -> {
                    FollowersPage(
                        relationViewModel = relationViewModel,
                    )
                }
            }
        }
    }
}