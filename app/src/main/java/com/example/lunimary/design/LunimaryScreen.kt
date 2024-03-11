package com.example.lunimary.design

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.lunimary.LocalNavNavController
import com.example.lunimary.R
import com.example.lunimary.models.User
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.ui.navToLogin
import com.example.lunimary.util.UserState
import com.example.lunimary.util.empty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

            networkError -> {
                ShowReasonForNoContent(
                    id = R.drawable.network_error,
                    description = stringResource(id = R.string.not_connected),
                    modifier = boxModifier
                )
            }

            error -> {
                ShowReasonForNoContent(
                    description = errorMsg ?: stringResource(id = R.string.error_occur_msg),
                    id = R.drawable.empty,
                    modifier = boxModifier
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
    @DrawableRes id: Int
) {
    Box(modifier = modifier.size(height = 250.dp, width = 200.dp)) {
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


data class SnackbarData(
    val msg: String,
    val actionLabel: String? = null
)