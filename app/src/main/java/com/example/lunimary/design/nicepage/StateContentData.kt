package com.example.lunimary.design.nicepage

import androidx.compose.ui.graphics.Color

/**
 * @property isError 是否展示错误插画
 * @property isEmpty true->没有内容展示时显示空白插画
 * @property isSearchEmpty 搜索不到相关内容
 * @property showShimmer 是否展示shimmer
 * @property openLoadingWheelDialog 是否展示加载中弹窗
 * @property color content 背景色
 * @property shouldCheckLoginState 是否检查登录状态，如果没登录则跳转登录页面
 * @property networkState 如果不为[NetworkState.Default]，则展示网络错误插画
 */
data class StateContentData(
    val isError: Boolean = false,
    val errorMsg: String? = null,
    val onRefreshClick: () -> Unit = {},
    val onRetryClick: () -> Unit = {},
    val isEmpty: Boolean = false,
    val emptyMsg: String? = null,

    val isSearchEmpty: Boolean = false,

    val showShimmer: Boolean = false,

    val openLoadingWheelDialog: Boolean = false,

    val color: Color = Color.Transparent,

    val shouldCheckLoginState: Boolean = false,

    val networkState: NetworkState = NetworkState.Default,
)