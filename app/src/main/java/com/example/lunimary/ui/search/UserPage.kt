package com.example.lunimary.ui.search

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.User
import com.example.lunimary.ui.common.UserItem

@Composable
fun UserPage(
    userItems: LazyPagingItems<User>,
    onItemClick: (User) -> Unit,
    viewModel: SearchViewModel
) {
    LunimaryPagingContent(
        items = userItems,
        key = { userItems[it]?.id!! },
        shimmer = false,
        searchEmptyEnabled = true,
        viewModel = viewModel,
        pagingKey = "UserPage_SearchViewModel"
    ) {
        UserItem(
            user = it,
            onItemClick = onItemClick
        )
    }
}