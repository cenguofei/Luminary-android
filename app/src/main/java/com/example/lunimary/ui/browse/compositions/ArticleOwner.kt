package com.example.lunimary.ui.browse.compositions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lunimary.design.components.UserHeadImage
import com.example.lunimary.ui.browse.BrowseViewModel
import com.example.lunimary.ui.browse.UiState

@Composable
fun ArticleOwner(
    onFollowClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    uiState: UiState,
    browseViewModel: BrowseViewModel,
    onUserClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserHeadImage(
            model = browseViewModel.articleOwner.value.realHeadUrl(),
            size = 55.dp,
            onClick = onUserClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = browseViewModel.articleOwner.value.username,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                val color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                Text(
                    text = uiState.article.niceDate,
                    color = color,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    tint = color
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = uiState.article.viewsNum.toString(),
                    color = color,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        FollowSettingButton(
            owner = browseViewModel.articleOwner.value,
            hasFetchedFriendship = uiState.hasFetchedFriendship,
            isFollowByMe = uiState.isFollowByMe,
            onUnfollowClick = onUnfollowClick,
            onFollowClick = onFollowClick
        )
    }
}