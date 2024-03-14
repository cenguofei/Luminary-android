package com.example.lunimary.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lunimary.design.LunimaryPagingScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RecommendPage(
    recommendViewModel: RecommendViewModel,
    coroutineScope: CoroutineScope,
    isOffline: StateFlow<Boolean>,
) {
    LaunchedEffect(
        key1 = Unit,
        block = {
            recommendViewModel.recommendedArticles()
        }
    )
    val articles = recommendViewModel.articles.collectAsLazyPagingItems()
    val offline = isOffline.collectAsStateWithLifecycle()

    LunimaryPagingScreen(items = articles, networkError = offline.value) {
        ArticleItem(onItemClick = { /*TODO*/ }, article = it)
    }
}
