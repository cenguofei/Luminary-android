package com.example.lunimary.ui.browse.compositions

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.example.lunimary.design.components.BackButton
import com.example.lunimary.design.components.UserHeadImage
import com.example.lunimary.model.Article
import com.example.lunimary.model.User
import com.example.lunimary.ui.browse.BrowseViewModel
import com.example.lunimary.ui.browse.UiState
import com.example.lunimary.ui.edit.EditType
import com.example.lunimary.util.notNull

@Composable
fun DynamicToolBar(
    listState: LazyListState,
    onBack: () -> Unit,
    onFollowClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    uiState: UiState,
    browseViewModel: BrowseViewModel,
    onUserClick: (User) -> Unit,
    navToEdit: (EditType, Article) -> Unit,
    onShowSnackbar: (msg: String, label: String?) -> Unit
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
        Spacer(modifier = Modifier.width(8.dp))
        BackButton(
            modifier = Modifier,
            onClick = onBack,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .alpha(if (!uiState.article.isLunimaryStation) 1f else alphaAnim.value),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserHeadImage(
                model = browseViewModel.articleOwner.value.realHeadUrl().notNull,
                size = 35.dp,
                onClick = { onUserClick(browseViewModel.articleOwner.value) }
            )
            Spacer(modifier = Modifier.weight(1f))
            FollowSettingButton(
                owner = browseViewModel.articleOwner.value,
                hasFetchedFriendship = uiState.hasFetchedFriendship,
                isFollowByMe = uiState.isFollowByMe,
                onUnfollowClick = onUnfollowClick,
                onFollowClick = onFollowClick
            )
        }
        ArticleOptions(
            uiState = uiState,
            browseViewModel = browseViewModel,
            navToEdit = navToEdit,
            onShowSnackbar = onShowSnackbar
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
}