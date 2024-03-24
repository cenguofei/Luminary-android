package com.example.lunimary.ui.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lunimary.R
import com.example.lunimary.models.Article
import com.example.lunimary.util.notNull

@Composable
fun RoundedCornerContent(
    uiState: State<UserUiState?>,
    userDetailViewModel: UserDetailViewModel,
    onDraftClick: () -> Unit,
    onItemClick: (Article) -> Unit,
    onRelationClick: (UserDataType) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(8.dp))
        UserData(
            uiState = uiState.value.notNull,
            onItemClick = onRelationClick
        )
        AboutArticles(
            coroutineScope = coroutineScope,
            userDetailViewModel = userDetailViewModel,
            onDraftClick = onDraftClick,
            onItemClick = onItemClick
        )
    }
}

@Composable
fun UserData(
    uiState: UserUiState,
    onItemClick: (UserDataType) -> Unit
) {
    Row(modifier = Modifier.padding(start = 20.dp)) {
        Item(
            num = uiState.likesOfUserArticles.toString(),
            text = stringResource(id = R.string.likes_of_articles),
            onClick = { onItemClick(UserDataType.Likes) }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Item(
            num = uiState.friendsNum.toString(),
            text = stringResource(id = R.string.friends),
            onClick = { onItemClick(UserDataType.Friends) }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Item(
            num = uiState.followingNum.toString(),
            text = stringResource(id = R.string.followings),
            onClick = { onItemClick(UserDataType.Follow) }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Item(
            num = uiState.followersNum.toString(),
            text = stringResource(id = R.string.followers),
            onClick = { onItemClick(UserDataType.Followers) }
        )
    }
}

@Composable
fun Item(
    num: String,
    text: String,
    onClick: () -> Unit = {}
) {
    TextButton(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = num,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                fontSize = 16.sp
            )
        }
    }
}
