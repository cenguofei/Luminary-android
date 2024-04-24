package com.example.lunimary.design.nicepage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.design.LoadingDialog
import com.example.lunimary.design.ShimmerList
import com.example.lunimary.util.empty


@Composable
fun LunimaryStateContent(
    modifier: Modifier = Modifier,
    stateContentData: StateContentData,
    content: @Composable ColumnScope.() -> Unit
) {
    LunimaryStateContent(
        modifier = modifier,
        error = stateContentData.isError,
        errorMsg = stateContentData.errorMsg,
        onRetryClick = stateContentData.onRetryClick,
        onRefreshClick = stateContentData.onRefreshClick,
        empty = stateContentData.isEmpty,
        emptyMsg = stateContentData.emptyMsg,
        searchEmpty = stateContentData.isSearchEmpty,
        shimmer = stateContentData.showShimmer,
        openLoadingWheelDialog = stateContentData.openLoadingWheelDialog,
        color = stateContentData.color,
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
    empty: Boolean = false,
    emptyMsg: String? = null,
    searchEmpty: Boolean = false,
    shimmer: Boolean = false,
    openLoadingWheelDialog: Boolean = false,
    color: Color = Color.Transparent,
    networkState: NetworkState = NetworkState.Default,
    onRefreshClick: () -> Unit = {},
    onRetryClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    LoadingDialog(show = openLoadingWheelDialog)
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
                    modifier = boxModifier,
                    refreshEnabled = false
                )
            }

            shimmer -> {
                ShimmerList()
            }

            searchEmpty -> {
                ShowReasonForNoContent(
                    id = R.drawable.empty_search,
                    description = stringResource(id = R.string.search_empty),
                    modifier = boxModifier,
                    refreshEnabled = false
                )
            }

            error -> {
                ShowReasonForNoContent(
                    description = if (errorMsg.isNullOrEmpty()) stringResource(id = R.string.error_occur_msg) else errorMsg,
                    id = R.drawable.empty,
                    modifier = boxModifier,
                    refreshEnabled = false,
                    retryEnabled = true,
                    onRetryClick = onRetryClick
                )
            }

            empty -> {
                ShowReasonForNoContent(
                    description = if (emptyMsg.isNullOrEmpty()) stringResource(id = R.string.empty_msg) else emptyMsg,
                    id = R.drawable.empty,
                    modifier = boxModifier,
                    refreshEnabled = true,
                    onRefreshClick = onRefreshClick
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

data class NetworkState(
    val msg: String = empty,
    val networkError: Boolean = false
) {
    companion object {
        val Default = NetworkState()
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