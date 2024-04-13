package com.example.lunimary.ui.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lunimary.R
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.model.Article
import com.example.lunimary.model.ext.InteractionData
import com.example.lunimary.ui.edit.EditType

@Composable
fun RoundedCornerContent(
    uiState: InteractionData,
    userDetailViewModel: UserDetailViewModel,
    onDraftClick: () -> Unit,
    onItemClick: (PageItem<Article>) -> Unit,
    onRelationClick: (UserDataType) -> Unit,
    navToEdit: (EditType, PageItem<Article>) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(8.dp))
        UserData(
            uiState = uiState,
            onItemClick = onRelationClick
        )
        AboutArticles(
            coroutineScope = coroutineScope,
            userDetailViewModel = userDetailViewModel,
            onDraftClick = onDraftClick,
            onItemClick = onItemClick,
            navToEdit = navToEdit
        )
    }
}

@Composable
fun UserData(
    uiState: InteractionData,
    onItemClick: (UserDataType) -> Unit
) {
    Row(modifier = Modifier.padding(start = 20.dp)) {
        Item(
            num = uiState.likeNum.toString(),
            text = stringResource(id = R.string.likes_of_articles),
            onClick = { onItemClick(UserDataType.Likes) }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Item(
            num = uiState.friendNum.toString(),
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
            num = uiState.followerNum.toString(),
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
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = Color.Transparent
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            Text(
                text = num,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}
