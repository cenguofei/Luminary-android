package com.example.lunimary.ui.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.lunimary.R
import com.example.lunimary.base.currentUser
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.common.RelationPageType

@Composable
fun UserDetailScreen(
    onOpenMenu: () -> Unit,
    onDraftClick: () -> Unit,
    appState: LunimaryAppState,
) {
    val user = currentUser
    val userDetailViewModel: UserDetailViewModel = viewModel()
    val showLikesDialog = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit, block = { userDetailViewModel.requestData() })
    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier) {
            UserBackground(
                modifier = Modifier
            )
            MenuButton(
                onOpenMenu = onOpenMenu,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
            )
        }
        UserDetailContent(
            modifier = Modifier,
            user = user,
            userDetailViewModel = userDetailViewModel,
            onDraftClick = onDraftClick,
            appState = appState,
            onRelationClick = {
                when(it) {
                    UserDataType.Friends -> {
                        appState.navToRelation(RelationPageType.Friends)
                    }
                    UserDataType.Follow -> {
                        appState.navToRelation(RelationPageType.Follow)
                    }
                    UserDataType.Followers -> {
                        appState.navToRelation(RelationPageType.Followers)
                    }
                    UserDataType.Likes -> { showLikesDialog.value = true }
                }
            },
            onClick = { appState.navToInformation() }
        )
    }
    ShowLikesDialog(
        showLikesDialog = showLikesDialog,
        likes = userDetailViewModel.uiState.value!!.likesOfUserArticles
    )
}

@Composable
fun ShowLikesDialog(
    showLikesDialog: MutableState<Boolean>,
    likes: Long
) {
    if (showLikesDialog.value) {
        Dialog(
            onDismissRequest = { showLikesDialog.value = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val width = (LocalConfiguration.current.screenWidthDp * 0.8).dp
            Surface(modifier = Modifier.width(width)) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.likes_bg),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "\"${currentUser.username}\"�����${likes}����",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    TextButton(onClick = { showLikesDialog.value = false }) {
                        Text(
                            text = stringResource(id = R.string.ok),
                            fontWeight = FontWeight.W600
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
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
            modifier = Modifier.fillMaxSize(),
            placeholder = painterResource(id = R.drawable.user_background),
            error = painterResource(id = R.drawable.user_background),
            fallback = painterResource(id = R.drawable.user_background)
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
