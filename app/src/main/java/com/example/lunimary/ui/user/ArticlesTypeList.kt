package com.example.lunimary.ui.user

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.lunimary.ui.home.bottomBarHeight

@Composable
fun ArticlesTypeList(
    tabs: List<ArticlesType>,
    index: Int,
    userDetailViewModel: UserDetailViewModel,
    onDraftClick: () -> Unit,
) {
    val modifier = Modifier.padding(bottom = bottomBarHeight)
    when (tabs[index]) {
        ArticlesType.Composition -> {
            PublicPage(
                userDetailViewModel = userDetailViewModel,
                onItemClick = {

                },
                modifier = modifier,
                onDraftClick = onDraftClick
            )
        }

        ArticlesType.Privacy -> {
            PrivacyPage(
                userDetailViewModel = userDetailViewModel,
                modifier = modifier,
                onItemClick = {

                }
            )
        }

        ArticlesType.Collect -> {
            CollectPage(
                userDetailViewModel = userDetailViewModel,
                modifier = modifier,
                onItemClick = {

                }
            )
        }

        ArticlesType.Like -> {
            LikePage(
                userDetailViewModel = userDetailViewModel,
                modifier = modifier,
                onItemClick = {

                }
            )
        }
    }
}


