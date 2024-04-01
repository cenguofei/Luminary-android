package com.example.lunimary.ui.user

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.models.Article
import com.example.lunimary.ui.home.bottomBarHeight

@Composable
fun ArticlesTypeList(
    tabs: List<ArticlesType>,
    index: Int,
    userDetailViewModel: UserDetailViewModel,
    onDraftClick: () -> Unit,
    onItemClick: (Article) -> Unit
) {
    val likesOfUser = userDetailViewModel.likesOfUser.collectAsLazyPagingItems()
    val collectsOfUser = userDetailViewModel.collectsOfUser.collectAsLazyPagingItems()
    val privacyArticlesState = userDetailViewModel.privacyArticles.collectAsLazyPagingItems()
    val compositionsState = userDetailViewModel.publicArticles.collectAsLazyPagingItems()
    DisposableEffect(
        key1 = Unit,
        effect = {
            userDetailViewModel.registerOnHaveNetwork(
                listOf(
                    "PublicPage" to compositionsState,
                    "PrivacyPage" to privacyArticlesState,
                    "CollectPage" to collectsOfUser,
                    "LikePage" to likesOfUser,
                )
            )
            onDispose {
                userDetailViewModel.unregisterOnHaveNetwork("PublicPage", "PrivacyPage", "CollectPage", "LikePage")
            }
        }
    )

    val modifier = Modifier.padding(bottom = bottomBarHeight)
    when (tabs[index]) {
        ArticlesType.Composition -> {
            PublicPage(
                compositionsState = compositionsState,
                onItemClick = onItemClick,
                modifier = modifier,
                onDraftClick = onDraftClick
            )
        }

        ArticlesType.Privacy -> {
            PrivacyPage(
                privacyArticlesState = privacyArticlesState,
                modifier = modifier,
                onItemClick = onItemClick
            )
        }

        ArticlesType.Collect -> {
            CollectPage(
                collectsOfUser = collectsOfUser,
                modifier = modifier,
                onItemClick = onItemClick
            )
        }

        ArticlesType.Like -> {
            LikePage(
                likesOfUser = likesOfUser,
                modifier = modifier,
                onItemClick = onItemClick
            )
        }
    }
}


