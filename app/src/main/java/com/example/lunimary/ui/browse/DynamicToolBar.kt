package com.example.lunimary.ui.browse

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.app.ShareCompat
import com.example.lunimary.R
import com.example.lunimary.design.LinearButton
import com.example.lunimary.design.UserHeadImage
import com.example.lunimary.design.cascade.CascadeMenu
import com.example.lunimary.design.cascade.cascadeMenu
import com.example.lunimary.models.User
import com.example.lunimary.util.currentUser
import com.example.lunimary.util.notNull

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DynamicToolBar(
    listState: LazyListState,
    onBack: () -> Unit,
    onFollowClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    uiState: UiState,
    browseViewModel: BrowseViewModel,
) {
    val firstVisibleIndex = remember { derivedStateOf { listState.firstVisibleItemIndex } }
    val alphaAnim = animateFloatAsState(
        targetValue = if (firstVisibleIndex.value >= 2) 1f else 0f,
        animationSpec = tween(durationMillis = 500)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier,
            onClick = onBack
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .alpha(alphaAnim.value)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                UserHeadImage(
                    model = browseViewModel.articleOwner.value.realHeadUrl().notNull,
                    size = 40.dp
                )
                Spacer(modifier = Modifier.weight(1f))
                FollowOrUnfollow(
                    owner = browseViewModel.articleOwner.value,
                    hasFetchedFriendship = uiState.hasFetchedFriendship,
                    isFollowByMe = uiState.isFollowByMe,
                    onUnfollowClick = onUnfollowClick,
                    onFollowClick = onFollowClick
                )
            }
        }

        val showOptions = remember { mutableStateOf(false) }
        val shareArticle = remember { mutableStateOf(false) }
        val appName = stringResource(id = R.string.app_name)
        val context = LocalContext.current
        LaunchedEffect(
            key1 = shareArticle.value,
            block = {
                if (shareArticle.value) {
                    val intent = ShareCompat.IntentBuilder(context)
                        .setType("text/plain")
                        .setText("""
            欢迎来到Lunimary-Blog！ 使用Lunimary-Blog App搜索"${uiState.article.title}"，获取更多资讯内容。
        """.trimIndent())
                        .setChooserTitle(appName)
                        .createChooserIntent()
                    context.startActivity(intent)
                    shareArticle.value = !shareArticle.value
                }
            }
        )
        Box(contentAlignment = Alignment.TopEnd) {
            CascadeMenu(
                isOpen = showOptions.value,
                menu = cascadeMenu {
                    item("share", "分享") {
                        icon(Icons.Default.Share)
                    }
                },
                onItemSelected = {
                    if (it == "share") {
                        shareArticle.value = true
                    }
                    showOptions.value = !showOptions.value
                },
                onDismiss = {
                    showOptions.value = !showOptions.value
                },
                offset = DpOffset(8.dp, 0.dp)
            )
            IconButton(onClick = { showOptions.value = !showOptions.value }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
fun FollowOrUnfollow(
    owner: User,
    hasFetchedFriendship: Boolean,
    isFollowByMe: Boolean,
    onUnfollowClick: () -> Unit,
    onFollowClick: () -> Unit,
) {
    if (currentUser.username != owner.username && hasFetchedFriendship) {
        if (isFollowByMe) {
            LinearButton(
                onClick = onUnfollowClick,
                text = stringResource(id = R.string.unfollow),
                modifier = Modifier
                    .width(45.dp)
                    .height(20.dp),
                textStyle = MaterialTheme.typography.bodySmall,
            )
        } else {
            LinearButton(
                onClick = onFollowClick,
                text = stringResource(id = R.string.follow),
                modifier = Modifier
                    .width(45.dp)
                    .height(20.dp),
                textStyle = MaterialTheme.typography.bodySmall,
            )
        }
    }
}