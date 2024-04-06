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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.base.mmkv.DarkThemeSetting
import com.example.lunimary.base.mmkv.SettingMMKV
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.design.LunimaryWebView
import com.example.lunimary.models.Article
import com.kevinnzou.web.rememberWebViewState
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun BodyContent(
    article: Article,
    onBack: () -> Unit,
    commentsSize: MutableState<Int>,
    browseViewModel: BrowseViewModel
) {
    if (article.isLunimaryStation) {
        val systemDarkMode = isSystemInDarkTheme()
        MarkdownText(
            modifier = Modifier.padding(horizontal = 16.dp),
            markdown = article.body,
            linkColor = when {
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
            style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
//                    onLinkClicked = onLinkClick
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 25.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = "ÆÀÂÛ ${commentsSize.value}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        LaunchedEffect(
            key1 = Unit,
            block = {
                browseViewModel.getAllCommentsOfArticle(article.id)
            }
        )
    } else if (article.link.isNotEmpty()) {
        val state = rememberWebViewState(url = article.link)
        LunimaryWebView(
            onExit = onBack,
            showToolbar = false,
            state = state
        )
        if (state.isLoading) {
            Box(Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.size(40.dp).align(Alignment.Center)) {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }
            }
        }
        if (!state.isLoading) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 25.dp, start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = "ÆÀÂÛ ${commentsSize.value}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            LaunchedEffect(
                key1 = Unit,
                block = {
                    browseViewModel.getAllCommentsOfArticle(article.id)
                }
            )
        }
    }


    when (browseViewModel.comments.value) {
        is NetworkResult.Loading -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(30.dp)) {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }
            }
        }

        is NetworkResult.Success -> {
            browseViewModel.comments.value as NetworkResult.Success
            val data = browseViewModel.comments.value.data ?: return
            val flatComments = browseViewModel.transform(data)
            commentsSize.value = flatComments.size
            flatComments.forEach {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    CommentItem(
                        modifier = Modifier,
                        comment = it.second,
                        commentOwner = it.first
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        is NetworkResult.Error -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.load_comments_error),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        else -> {}
    }
}