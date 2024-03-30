package com.example.lunimary.ui.relation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.design.LunimaryPagingContent
import com.example.lunimary.models.User
import com.example.lunimary.ui.common.UserItem

@Composable
fun FriendsPage(
    relationViewModel: RelationViewModel,
    onItemClick: (User) -> Unit
) {
    val friends = relationViewModel.friends.collectAsLazyPagingItems()
    LunimaryPagingContent(
        items = friends,
        topItem = { Spacer(modifier = Modifier.height(16.dp)) },
        viewModel = relationViewModel,
        pagingKey = "FriendsPage_RelationViewModel"
    ) {
        Column {
            UserItem(user = it.user, onItemClick = onItemClick)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}