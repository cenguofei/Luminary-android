package com.example.lunimary.design.nicepage

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.R
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.ScopeViewModel
import com.example.lunimary.base.network.isCurrentlyConnected
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.design.components.Footer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> LunimaryPagingContent(
    modifier: Modifier = Modifier,
    openLoadingWheelDialog: Boolean = false,
    color: Color = Color.Transparent,
    searchEmptyEnabled: Boolean = false,
    shimmer: Boolean = true,
    key: ((index: Int) -> Any)? = null,
    topItem: (@Composable () -> Unit)? = null,
    items: LazyPagingItems<PageItem<T>>,
    viewModel: BaseViewModel = ScopeViewModel,
    pagingKey: String? = null,
    refreshEnabled: Boolean = true,
    itemContent: @Composable (index: Int, item: PageItem<T>) -> Unit
) {
    Effect(pagingKey = pagingKey, viewModel = viewModel) { items.retry() }
    val loadState = items.loadState
    val showError = (items.loadState.refresh is LoadState.Error) && items.isEmpty()
    val showErrorMsg = when {
        (loadState.refresh is LoadState.Error) -> {
            (loadState.refresh as LoadState.Error).error.message
        }

        else -> stringResource(id = R.string.load_error_and_retry)
    }
    val showShimmer = if (shimmer) loadState.refresh is LoadState.Loading else false
    val isAllItemDeleted = items.itemSnapshotList.all { it?.deleted == true }
    val showEmpty = (loadState.refresh is LoadState.NotLoading && items.isEmpty()) || isAllItemDeleted
    val hasNetwork = LocalContext.current.isCurrentlyConnected()
    val showNetworkError = !hasNetwork && items.isEmpty()
    val searchEmpty = if (searchEmptyEnabled) showEmpty else false

    val state = rememberPullToRefreshState { refreshEnabled && !showShimmer }
    val scaleFraction = if (state.isRefreshing) 1f else
        LinearOutSlowInEasing.transform(state.progress).coerceIn(0f, 1f)
    if (state.isRefreshing) {
        LaunchedEffect(key1 = state.isRefreshing, block = { items.refresh() })
    }
    val isNotLoading = items.loadState.refresh is LoadState.NotLoading
    val isError = items.loadState.refresh is LoadState.Error
    if (isNotLoading || isError) {
        LaunchedEffect(key1 = Unit, block = { state.endRefresh() })
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(state.nestedScrollConnection)
    ) {
        LunimaryStateContent(
            modifier = modifier,
            error = showError, // 没有数据并且出错才展示空页面
            errorMsg = showErrorMsg,
            onErrorClick = { items.refresh() },
            empty = showEmpty,
            networkState = if (showNetworkError) NetworkState(
                msg = stringResource(id = R.string.not_connected),
                networkError = true
            ) else NetworkState.Default,
            shimmer = showShimmer,
            searchEmpty = searchEmpty,
            openLoadingWheelDialog = openLoadingWheelDialog,
            color = color,
            onRefreshClick = { items.refresh() },
            onRetryClick = { items.retry() }
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                topItem?.let { item { it() } }
                if (!state.isRefreshing) {
                    items(items.itemCount, key = key) { index ->
                        items[index]?.let { item ->
                            if (!item.deleted) {
                                itemContent(index, item)
                            }
                        }
                    }
                }
                footer(loadState = loadState, items = items, hasNetwork = hasNetwork)
            }
        }

        PullToRefreshContainer(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .graphicsLayer(scaleX = scaleFraction, scaleY = scaleFraction),
            state = state,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        )
    }
}

private fun <T : Any> LazyListScope.footer(
    loadState: CombinedLoadStates,
    items: LazyPagingItems<PageItem<T>>,
    hasNetwork: Boolean,
) {
    when {
        loadState.append is LoadState.NotLoading -> {
            if ((loadState.append as LoadState.NotLoading).endOfPaginationReached) {
                //没有更多
                item { Footer(desc = stringResource(id = R.string.loading_no_more)) }
            }
        }

        !hasNetwork && items.isNotEmpty() -> {
            //没有网，但是前面请求的数据还在
            item { Footer(desc = stringResource(id = R.string.load_error_for_no_net)) }
        }

        loadState.append is LoadState.Loading -> {
            //底部加载
            item { Footer(desc = stringResource(id = R.string.loading)) }
        }

        items.loadState.append is LoadState.Error -> {
            //底部错误，点击重试
            item {
                Footer(
                    desc = stringResource(id = R.string.load_error_and_retry),
                    enabled = true,
                    onClick = { items.retry() }
                )
            }
        }
    }
}

@Composable
private fun Effect(
    pagingKey: String?,
    viewModel: BaseViewModel,
    onRetry: () -> Unit
) {
    pagingKey?.let {
        DisposableEffect(
            key1 = Unit,
            effect = {
                viewModel.registerOnHaveNetwork(key = pagingKey, noNetToHaveNet = onRetry)
                onDispose { viewModel.unregisterOnHaveNetwork(pagingKey) }
            }
        )
    }
}

@Composable
private fun <T : Any> AnimatedItem(
    pageItem: PageItem<T>,
    index: Int,
    itemContent: @Composable (index: Int, item: PageItem<T>) -> Unit
) {
    AnimatedContent(
        targetState = pageItem.deleted,
        transitionSpec = {
            (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                    scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)))
                .togetherWith(fadeOut(animationSpec = tween(90)))
        },
    ) {
        if (!it) {
            itemContent(index, pageItem)
        }
    }
}