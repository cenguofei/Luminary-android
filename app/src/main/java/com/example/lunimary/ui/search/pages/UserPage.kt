package com.example.lunimary.ui.search.pages

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.nicepage.LunimaryPagingContent
import com.example.lunimary.model.User
import com.example.lunimary.ui.common.UserItem

@Composable
fun UserPage(
    userItems: LazyPagingItems<PageItem<User>>,
    onItemClick: (User) -> Unit,
) {
    LunimaryPagingContent(
        items = userItems,
        key = { userItems[it]?.data?.id!! },
        shimmer = false,
        searchEmptyEnabled = true,
        refreshEnabled = false
    ) { _, item ->
        UserItem(
            user = item.data,
            onItemClick = onItemClick
        )
    }
}