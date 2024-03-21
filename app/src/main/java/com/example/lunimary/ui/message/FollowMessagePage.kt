package com.example.lunimary.ui.message

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.lunimary.design.LunimaryScreen
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.network.isCurrentlyConnected

@Composable
fun FollowMessagePage(messageViewModel: MessageViewModel) {
    LaunchedEffect(
        key1 = Unit,
        block = { messageViewModel.messageForFollows() }
    )
    val networkResult = messageViewModel.followMessage.value
    LunimaryScreen(
        networkResult = networkResult,
        networkError = !LocalContext.current.isCurrentlyConnected()
    ) {
        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
            when(networkResult) {
                is NetworkResult.Success -> {
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    items(messageViewModel.transformFollowData()) { item ->
                        FollowItem(item = item)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                else -> {}
            }
        }
    }
}
