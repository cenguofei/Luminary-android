package com.example.lunimary.ui.browse

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.example.lunimary.design.BackButton
import com.example.lunimary.design.UserHeadImage
import com.example.lunimary.models.Article
import com.example.lunimary.models.User
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
    onArticleDeleted: (Article) -> Unit,
    navToEdit: (EditType, Article) -> Unit
) {
    val article = uiState.article
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
        BackButton(
            modifier = Modifier,
            onClick = onBack,
            tint = MaterialTheme.colorScheme.onSurface
        )
        if (article.isLunimaryStation) {
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
                        size = 40.dp,
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
            }
        } else {
            Text(
                text = article.author,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        ArticleOptions(
            uiState = uiState,
            browseViewModel = browseViewModel,
            onArticleDeleted = onArticleDeleted,
            navToEdit = navToEdit
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
}