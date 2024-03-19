package com.example.lunimary.design

import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
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
import com.example.lunimary.LocalNavNavController
import com.example.lunimary.R
import com.example.lunimary.models.User
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.network.isCurrentlyConnected
import com.example.lunimary.ui.navToLogin
import com.example.lunimary.util.UserState
import com.example.lunimary.util.empty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun <T : Any> LunimaryPagingScreen(
    modifier: Modifier = Modifier,
    networkError: Boolean = false,
    noMessage: Boolean = false,
    snackbarData: SnackbarData? = null,
    openLoadingWheelDialog: Boolean = false,
    coroutine: CoroutineScope = rememberCoroutineScope(),
    color: Color = Color.Transparent,
    checkLoginState: Boolean = false,
    items: LazyPagingItems<T>,
    key: ((index: Int) -> Any)? = null,
    itemContent: @Composable (T) -> Unit
) {
    val loadState = items.loadState
    val showError = items.loadState.refresh is LoadState.Error && items.isEmpty()
    val showErrorMsg = if (loadState.refresh is LoadState.Error) {
        (loadState.refresh as LoadState.Error).error.message
    } else stringResource(id = R.string.load_error_and_retry)
    val showShimmer = loadState.refresh is LoadState.Loading
    val showEmpty = loadState.refresh is LoadState.NotLoading && items.isEmpty()
    LunimaryScreen(
        modifier = modifier,
        error = showError, // 没有数据并且出错才展示空页面
        errorMsg = showErrorMsg,
        onErrorClick = { items.refresh() },
        empty = showEmpty,
        networkError = networkError && items.isEmpty(),
        searchEmpty = showEmpty,
        noMessage = noMessage,
        shimmer = showShimmer,
        snackbarData = snackbarData,
        openLoadingWheelDialog = openLoadingWheelDialog,
        coroutine = coroutine,
        color = color,
        checkLoginState = checkLoginState
    ) {
        val hasNetwork = LocalContext.current.isCurrentlyConnected()
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(items.itemCount, key = key) {
                items[it]?.let { item -> itemContent(item) }
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
    }
}

/**
 * @param error 错误页的插图
 * @param empty 空白页的插图
 * @param networkError 没有网络时展示的插图
 * @param searchEmpty 没有搜索到任何内容时展示的内容
 * @param noMessage 没有消息时展示的内容
 * @param shimmer 网络请求期间展示的占位符
 */
@Composable
fun LunimaryScreen(
    modifier: Modifier = Modifier,
    error: Boolean = false,
    errorMsg: String? = null,
    onErrorClick: () -> Unit = {},
    empty: Boolean = false,
    emptyMsg: String? = null,
    networkError: Boolean = false,
    searchEmpty: Boolean = false,
    noMessage: Boolean = false,
    shimmer: Boolean = false,
    snackbarData: SnackbarData? = null,
    openLoadingWheelDialog: Boolean = false,
    coroutine: CoroutineScope = rememberCoroutineScope(),
    color: Color = Color.Transparent,
    checkLoginState: Boolean = false,
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
                duration = SnackbarDuration.Long,
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
        when {
            networkError -> {
                ShowReasonForNoContent(
                    id = R.drawable.network_error,
                    description = stringResource(id = R.string.not_connected),
                    modifier = boxModifier
                )
            }

            shimmer -> {
                ShimmerList()
            }

            noMessage -> {
                ShowReasonForNoContent(
                    id = R.drawable.empty_message,
                    description = stringResource(id = R.string.no_msg),
                    modifier = boxModifier
                )
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
                    description = errorMsg ?: stringResource(id = R.string.error_occur_msg),
                    id = R.drawable.empty,
                    modifier = boxModifier,
                    enabled = true,
                    onClick = onErrorClick
                )
            }

            empty -> {
                ShowReasonForNoContent(
                    description = emptyMsg ?: stringResource(id = R.string.empty_msg),
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

@Composable
fun <T> LunimaryScreen(
    modifier: Modifier = Modifier,
    networkResult: NetworkResult<T>,
    onErrorClick: () -> Unit = {},
    networkError: Boolean = false,
    searchEmpty: Boolean = false,
    openLoadingWheelDialog: Boolean = false,
    coroutine: CoroutineScope = rememberCoroutineScope(),
    color: Color = Color.Transparent,
    checkLoginState: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    LunimaryScreen(
        modifier = modifier,
        error = networkResult is NetworkResult.Error,
        errorMsg = (networkResult as? NetworkResult.Error)?.msg,
        onErrorClick = onErrorClick,
        empty = networkResult is NetworkResult.Empty,
        emptyMsg = (networkResult as? NetworkResult.Empty)?.msg,
        networkError = networkError,
        searchEmpty = searchEmpty,
        shimmer = networkResult is NetworkResult.Loading,
        openLoadingWheelDialog = openLoadingWheelDialog,
        coroutine = coroutine,
        color = color,
        checkLoginState = checkLoginState,
        content = content
    )
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