package com.example.lunimary.design

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.LiveData
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.lunimary.R
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.util.empty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * @param showLoadingWheel 是否展示加载中
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
    networkError: Boolean = false,
    searchEmpty: Boolean = false,
    noMessage: Boolean = false,
    shimmer: Boolean = false,
    snackbarData: SnackbarData? = null,
    openLoadingWheelDialog: Boolean = false,
    coroutine: CoroutineScope = rememberCoroutineScope(),
    content: @Composable ColumnScope.() -> Unit
) {
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
    Box(modifier = modifier.fillMaxSize()) {
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
                    description = stringResource(id = R.string.empty_msg),
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
fun ShowReasonForNoContent(
    modifier: Modifier = Modifier,
    description: String = empty,
    @DrawableRes id: Int
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(id), contentDescription = null)
        Text(text = description, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun LoadingDialog(show: Boolean = false) {
    if (show) {
        Dialog(properties = DialogProperties(dismissOnClickOutside = false), onDismissRequest = {}) {
            Surface(
                modifier = Modifier.size(height = 100.dp, width = 100.dp),
                shape = RoundedCornerShape(12),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    LoadingWheel()
                }
            }
        }
    }
}

@Composable
fun LoadingWheel(modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        val anim = rememberLottieAnimatable()
        val composition by rememberLottieComposition(LottieCompositionSpec.Asset("loading.json"))
        LaunchedEffect(composition) {
            anim.animate(
                composition,
                iterations = LottieConstants.IterateForever,
            )
        }
        LottieAnimation(anim.composition, { anim.progress }, Modifier.fillMaxSize())
    }
}

data class SnackbarData(
    val msg: String,
    val actionLabel: String? = null
)