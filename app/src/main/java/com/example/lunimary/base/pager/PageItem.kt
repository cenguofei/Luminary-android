package com.example.lunimary.base.pager

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

/**
 * Paging模型，承载与UI相关的状态数据
 */
data class PageItem<T : Any>(
    val data: T,
    val deleted: MutableState<Boolean> = mutableStateOf(false)
)