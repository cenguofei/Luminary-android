package com.example.lunimary.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lunimary.design.LunimaryScreen
import com.example.lunimary.design.ShimmerList
import com.example.lunimary.design.SnackbarData
import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.Page
import com.example.lunimary.models.responses.isEmpty
import com.example.lunimary.models.responses.isNotEmpty
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.network.asError
import com.example.lunimary.network.asSuccess
import com.example.lunimary.network.getErrorMsg
import com.example.lunimary.network.isError
import com.example.lunimary.network.isLoading
import com.example.lunimary.network.isSuccess
import com.example.lunimary.network.toSnackbarData
import com.example.lunimary.util.empty
import com.example.lunimary.util.logd
import com.example.lunimary.util.notNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RecommendPage(
    recommendViewModel: RecommendViewModel,
    coroutineScope: CoroutineScope
) {
    LaunchedEffect(
        key1 = Unit,
        block = {
            recommendViewModel.recommendedArticles()
        }
    )
    val pages by recommendViewModel.pageArticles.observeAsState()
    LunimaryScreen(
        shimmer = pages.isLoading(),
        error = pages.isError(),
        errorMsg = pages.getErrorMsg(),
        empty = pages.isSuccess() && pages.asSuccess().notNull.data.isEmpty(),
        coroutine = coroutineScope
    ) {
        LazyColumn {
            val data = pages.asSuccess()?.data
            data?.lists?.let {
                items(it) { article ->
                    ArticleItem(onItemClick = { /*TODO*/ }, article = article)
                }
            }
        }
    }
}