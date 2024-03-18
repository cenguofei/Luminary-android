package com.example.lunimary.ui.browse

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.base.DarkThemeSetting
import com.example.lunimary.base.SettingMMKV
import com.example.lunimary.models.Comment
import com.example.lunimary.models.User
import com.example.lunimary.network.NetworkResult
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun BrowseScreenContent(
    onBack: () -> Unit,
    onFollowClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    browseViewModel: BrowseViewModel,
    onEditCommentClick: () -> Unit
) {
    val uiState by browseViewModel.uiState.observeAsState()
    val listState = rememberLazyListState()
    val commentsSize = remember { mutableStateOf(0) }
    Column(modifier = Modifier.fillMaxSize()) {
        DynamicToolBar(
            onBack = onBack,
            onFollowClick = onFollowClick,
            onUnfollowClick = onUnfollowClick,
            listState = listState,
            uiState = uiState!!,
            browseViewModel = browseViewModel
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            state = listState
        ) {
            item { ArticleTitle(title = uiState!!.article.title) }
            item {
                AboutUser(
                    uiState = uiState!!,
                    onUnfollowClick = onUnfollowClick,
                    onFollowClick = onFollowClick,
                    browseViewModel = browseViewModel
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                val systemDarkMode = isSystemInDarkTheme()
                MarkdownText(
                    modifier = Modifier,
                    markdown = uiState!!.article.body,
                    linkColor = when  {
                        SettingMMKV.userHasSetTheme -> {
                            if (SettingMMKV.darkThemeSetting == DarkThemeSetting.DarkMode) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.Blue
                            }
                        }
                        systemDarkMode -> MaterialTheme.colorScheme.primary
                        else -> Color.Blue
                    },
                    style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface)
                )
            }

            item {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 25.dp)) {
                    Text(
                        text = "评论 ${commentsSize.value}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            when(browseViewModel.comments.value) {
                is NetworkResult.Loading -> {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Box(modifier = Modifier.size(30.dp)) {
                                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                }
                is NetworkResult.Success -> {
                    browseViewModel.comments.value as NetworkResult.Success
                    val data = browseViewModel.comments.value.data ?: return@LazyColumn
                    val flatComments = browseViewModel.transform(data)
                    commentsSize.value = flatComments.size
                    items(flatComments, key = { it.second.id }) {
                        CommentItem(
                            modifier = Modifier,
                            comment = it.second,
                            commentOwner = it.first
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                is NetworkResult.Error -> {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.load_comments_error),
                                color = Color.Red
                            )
                        }
                    }
                }
                else -> { }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        InteractionBottomBar(
            browseViewModel = browseViewModel,
            modifier = Modifier.padding(horizontal = 16.dp),
            onEditCommentClick = onEditCommentClick
        )
    }
}