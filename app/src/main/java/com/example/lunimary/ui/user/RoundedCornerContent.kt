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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.models.Article
import com.example.lunimary.util.notNull

@Composable
fun RoundedCornerContent(
    uiState: State<UserUiState?>,
    userDetailViewModel: UserDetailViewModel,
    onDraftClick: () -> Unit,
    onItemClick: (Article) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(8.dp))
        UserData(uiState = uiState.value.notNull)
        AboutArticles(
            coroutineScope = coroutineScope,
            userDetailViewModel = userDetailViewModel,
            onDraftClick = onDraftClick,
            onItemClick = onItemClick
        )
    }
}

@Composable
fun UserData(uiState: UserUiState) {
    Row(modifier = Modifier.padding(start = 20.dp, top = 12.dp)) {
        Item(
            num = uiState.likesOfUserArticles.toString(),
            text = stringResource(id = R.string.likes_of_articles)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Item(
            num = uiState.followings.size.toString(),
            text = stringResource(id = R.string.followings)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Item(
            num = uiState.followers.size.toString(),
            text = stringResource(id = R.string.followers)
        )
    }
}

@Composable
fun Item(
    num: String,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = num,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Text(text = text, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
    }
}
