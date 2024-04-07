package com.example.lunimary.base.pager

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

/**
 * Pagingģ�ͣ�������UI��ص�״̬����
 */
data class PageItem<T : Any>(
    val data: T,
    val deleted: MutableState<Boolean> = mutableStateOf(false)
)