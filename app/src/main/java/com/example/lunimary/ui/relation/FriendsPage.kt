package com.example.lunimary.ui.relation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.lunimary.design.LunimaryScreen
import com.example.lunimary.models.User
import com.example.lunimary.network.isCurrentlyConnected
import com.example.lunimary.ui.common.UserItem

@Composable
fun FriendsPage(
    relationViewModel: RelationViewModel,
    onItemClick: (User) -> Unit
) {
    LunimaryScreen(
        networkResult = relationViewModel.friends.value,
        networkError = !LocalContext.current.isCurrentlyConnected()
    ) {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)) {
            val data = relationViewModel.friends.value.data ?: emptyList()
            item { Spacer(modifier = Modifier.height(16.dp)) }
            items(data) {
                UserItem(user = it, onItemClick = onItemClick)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}