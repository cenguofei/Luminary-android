package com.example.lunimary.design

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.lunimary.R
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.ScopeViewModel
import com.example.lunimary.base.UserState
import com.example.lunimary.base.network.isCurrentlyConnected
import com.example.lunimary.models.User
import com.example.lunimary.ui.navToLogin
import com.example.lunimary.util.empty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> LunimaryPagingContent(
    modifier: Modifier = Modifier,
    snackbarData: SnackbarData? = null,
    openLoadingWheelDialog: Boolean = false,
    coroutine: CoroutineScope = rememberCoroutineScope(),
    color: Color = Color.Transparent,
    checkLoginState: Boolean = false,
    searchEmptyEnabled: Boolean = false,
    shimmer: Boolean = true,
    key: ((index: Int) -> Any)? = null,
    topItem: (@Composable () -> Unit)? = null,
    items: LazyPagingItems<T>,
    viewModel: BaseViewModel = ScopeViewModel,
    pagingKey: String? = null,
    refreshEnabled: Boolean = true,
    itemContent: @Composable (T) -> Unit
) {
    pagingKey?.let {
        DisposableEffect(
            key1 = Unit,
            effect = {
                viewModel.registerOnHaveNetwork(pagingKey) {
                    items.retry()
                }
                onDispose { viewModel.unregisterOnHaveNetwork(pagingKey) }
            }
        )
    }
    val loadState = items.loadState
    val showError = (items.loadState.refresh is LoadState.Error) && items.isEmpty()
    val showErrorMsg = when {
        (loadState.refresh is LoadState.Error) -> {
            (loadState.refresh as LoadState.Error).error.message
        }

        else -> stringResource(id = R.string.load_error_and_retry)
    }
    val showShimmer = if (shimmer) loadState.refresh is LoadState.Loading else false
    val showEmpty = loadState.refresh is LoadState.NotLoading && items.isEmpty()
    val hasNetwork = LocalContext.current.isCurrentlyConnected()
    val showNetworkError = !hasNetwork && items.isEmpty()
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
        searchEmpty = if (searchEmptyEnabled) showEmpty else false,
        snackbarData = snackbarData,
        openLoadingWheelDialog = openLoadingWheelDialog,
        coroutine = coroutine,
        color = color,
        checkLoginState = checkLoginState
    ) {
        val state = rememberPullToRefreshState { refreshEnabled }
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
        Box(modifier = Modifier
            .fillMaxSize()
            .nestedScroll(state.nestedScrollConnection)) {
            LazyColumn(Modifier.fillMaxSize()) {
                topItem?.let { item { it() } }
                if (!state.isRefreshing) {
                    items(items.itemCount, key = key) { index ->
                        items[index]?.let { item -> itemContent(item) }
                    }
                }
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
}

@Composable
fun LunimaryStateContent(
    modifier: Modifier = Modifier,
    stateContentData: StateContentData,
    coroutine: CoroutineScope = rememberCoroutineScope(),
    content: @Composable ColumnScope.() -> Unit
) {
    LunimaryStateContent(
        modifier = modifier,
        coroutine = coroutine,
        error = stateContentData.isError,
        errorMsg = stateContentData.errorMsg,
        onErrorClick = stateContentData.onErrorClick,
        empty = stateContentData.isEmpty,
        emptyMsg = stateContentData.emptyMsg,
        searchEmpty = stateContentData.isSearchEmpty,
        shimmer = stateContentData.showShimmer,
        snackbarData = stateContentData.snackbarData,
        openLoadingWheelDialog = stateContentData.openLoadingWheelDialog,
        color = stateContentData.color,
        checkLoginState = stateContentData.shouldCheckLoginState,
        networkState = stateContentData.networkState,
        content = content
    )
}

/**
 *
 * @param error 错误页的插图
 * @param empty 空白页的插图
 * @param searchEmpty 没有搜索到任何内容时展示的内容
 * @param shimmer 网络请求期间展示的占位符
 */
@Composable
fun LunimaryStateContent(
    modifier: Modifier = Modifier,
    error: Boolean = false,
    errorMsg: String? = null,
    onErrorClick: () -> Unit = {},
    empty: Boolean = false,
    emptyMsg: String? = null,
    searchEmpty: Boolean = false,
    shimmer: Boolean = false,
    snackbarData: SnackbarData? = null,
    openLoadingWheelDialog: Boolean = false,
    color: Color = Color.Transparent,
    checkLoginState: Boolean = false,
    networkState: NetworkState = NetworkState.Default,
    coroutine: CoroutineScope = rememberCoroutineScope(),
    content: @Composable ColumnScope.() -> Unit
) {
    if (checkLoginState) {
        CheckLoginState()
    }
    LoadingDialog(show = openLoadingWheelDialog)
    if (snackbarData != null) {
        val snackbarHostState = LocalSnackbarHostState.current.snackbarHostState
        coroutine.launch {
            snackbarHostState?.showSnackbar(
                message = snackbarData.msg,
                actionLabel = snackbarData.actionLabel,
                duration = SnackbarDuration.Short,
            )
        }
    }
    val absoluteElevation = LocalAbsoluteTonalElevation.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .surface(
                shape = RectangleShape,
                backgroundColor = surfaceColorAtElevation(
                    color = color,
                    elevation = absoluteElevation
                ),
                border = null,
                shadowElevation = 0.dp
            )
    ) {
        val boxModifier = Modifier.align(Alignment.Center)
        val hasNetErrorData = networkState != NetworkState.Default
        when {
            hasNetErrorData -> {
                val msg = networkState.msg.ifEmpty {
                    stringResource(id = R.string.not_connected)
                }
                ShowReasonForNoContent(
                    id = R.drawable.network_error,
                    description = msg,
                    modifier = boxModifier
                )
            }

            shimmer -> {
                ShimmerList()
            }

            searchEmpty -> {
                ShowReasonForNoContent(
                    id = R.drawable.empty_search,
                    description = stringResource(id = R.string.search_empty),
                    modifier = boxModifier
                )
            }

            error -> {
                ShowReasonForNoContent(
                    description = if (errorMsg.isNullOrEmpty()) stringResource(id = R.string.error_occur_msg) else errorMsg,
                    id = R.drawable.empty,
                    modifier = boxModifier,
                    enabled = true,
                    onClick = onErrorClick
                )
            }

            empty -> {
                ShowReasonForNoContent(
                    description = if (emptyMsg.isNullOrEmpty()) stringResource(id = R.string.empty_msg) else emptyMsg,
                    id = R.drawable.empty,
                    modifier = boxModifier
                )
            }

            else -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxSize(), content = content)
                }
            }
        }
    }
}

/**
 * @property isError 是否展示错误插画
 * @property isEmpty true->没有内容展示时显示空白插画
 * @property isSearchEmpty 搜索不到相关内容
 * @property showShimmer 是否展示shimmer
 * @property openLoadingWheelDialog 是否展示加载中弹窗
 * @property snackbarData 不为空则展示snackbar
 * @property color content 背景色
 * @property shouldCheckLoginState 是否检查登录状态，如果没登录则跳转登录页面
 * @property networkState 如果不为[NetworkState.Default]，则展示网络错误插画
 */
data class StateContentData(
    val isError: Boolean = false,
    val errorMsg: String? = null,
    val onErrorClick: () -> Unit = {},

    val isEmpty: Boolean = false,
    val emptyMsg: String? = null,

    val isSearchEmpty: Boolean = false,

    val showShimmer: Boolean = false,

    val openLoadingWheelDialog: Boolean = false,

    val snackbarData: SnackbarData? = null,

    val color: Color = Color.Transparent,

    val shouldCheckLoginState: Boolean = false,

    val networkState: NetworkState = NetworkState.Default,
)

data class NetworkState(
    val msg: String = empty,
    val networkError: Boolean = false
) {
    companion object {
        val Default = NetworkState()
    }
}

@Composable
fun CheckLoginState() {
    val userState = UserState.currentUserState.observeAsState()
    if (userState.value == User.NONE && UserState.updated) {
        val navController = LocalNavNavController.current
        navController.navToLogin()
    }
}

@Composable
private fun ShowReasonForNoContent(
    modifier: Modifier = Modifier,
    description: String = empty,
    @DrawableRes id: Int,
    onClick: () -> Unit = {},
    enabled: Boolean = false
) {
    Box(
        modifier = modifier
            .size(height = 250.dp, width = 200.dp)
            .clickable(
                role = Role.Button,
                onClick = onClick,
                enabled = enabled
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(id), contentDescription = null)
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun Modifier.surface(
    shape: Shape,
    backgroundColor: Color,
    border: BorderStroke?,
    shadowElevation: Dp
) = this
    .shadow(shadowElevation, shape, clip = false)
    .then(if (border != null) Modifier.border(border, shape) else Modifier)
    .background(color = backgroundColor, shape = shape)
    .clip(shape)

@Composable
private fun surfaceColorAtElevation(color: Color, elevation: Dp): Color {
    return if (color == MaterialTheme.colorScheme.surface) {
        MaterialTheme.colorScheme.surfaceColorAtElevation(elevation)
    } else {
        color
    }
}


fun <T : Any> LazyPagingItems<T>.isEmpty() = itemCount == 0

fun <T : Any> LazyPagingItems<T>.isNotEmpty() = !isEmpty()