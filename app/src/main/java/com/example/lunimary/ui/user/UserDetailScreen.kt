package com.example.lunimary.ui.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.lunimary.util.currentUser

@Composable
fun UserDetailScreen(
    onOpenMenu: () -> Unit,
    onDraftClick: () -> Unit,
) {
    val user = currentUser
    val userDetailViewModel: UserDetailViewModel = viewModel()
    LaunchedEffect(key1 = Unit, block = { userDetailViewModel.requestData() })
    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier) {
            UserBackground(
                modifier = Modifier
            )
            MenuButton(
                onOpenMenu = onOpenMenu,
                modifier = Modifier.align(Alignment.TopEnd)
                    .statusBarsPadding()
            )
        }
        UserDetailContent(
            modifier = Modifier,
            user = user,
            userDetailViewModel = userDetailViewModel,
            onDraftClick = onDraftClick
        )
    }
}

@Composable
fun UserBackground(modifier: Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        AsyncImage(
            model = currentUser.realBackgroundUrl(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun MenuButton(
    onOpenMenu: () -> Unit,
    modifier: Modifier
) {
    IconButton(
        onClick = onOpenMenu,
        modifier = modifier
    ) {
        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
    }
}
