package com.example.lunimary.ui.search

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.design.LunimaryPagingScreen
import com.example.lunimary.models.User
import com.example.lunimary.ui.relation.FriendItem
import kotlinx.coroutines.flow.StateFlow

@Composable
fun UserPage(
    isOffline: StateFlow<Boolean>,
    userItems: LazyPagingItems<User>
) {
    val offline = isOffline.collectAsStateWithLifecycle()
    LunimaryPagingScreen(
        items = userItems,
        networkError = offline.value,
        key = { userItems[it]?.id!! },
        shimmer = false,
        searchEmptyEnabled = true
    ) {
        FriendItem(user = it)
    }
}