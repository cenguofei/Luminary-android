package com.example.lunimary.ui.viewuser

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lunimary.R
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.models.Article
import com.example.lunimary.util.notNull

@Composable
fun ViewUserRoundedCornerContent(
    viewModel: ViewUserViewModel,
    onItemClick: (PageItem<Article>) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(8.dp))
        ViewUserData(
            uiState = viewModel.uiState.value.notNull
        )
        UserArticles(
            viewModel = viewModel,
            onItemClick = onItemClick,
            modifier = Modifier
        )
    }
}

@Composable
fun ViewUserData(
    uiState: UiState
) {
    Row(modifier = Modifier.padding(start = 20.dp)) {
        ViewUserItem(
            num = uiState.likeNum.toString(),
            text = stringResource(id = R.string.likes_of_articles),
        )
        Spacer(modifier = Modifier.width(12.dp))
        ViewUserItem(
            num = uiState.followNum.toString(),
            text = stringResource(id = R.string.followings),
        )
        Spacer(modifier = Modifier.width(12.dp))
        ViewUserItem(
            num = uiState.followersNum.toString(),
            text = stringResource(id = R.string.followers),
        )
    }
}

@Composable
fun ViewUserItem(
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
