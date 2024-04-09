package com.example.lunimary.base.pager

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.lunimary.util.logd

/**
 * Paging模型，承载与UI相关的状态数据
 */
data class PageItem<T : Any>(
    val fakeData: T
) {
    private val _deleted: MutableState<Boolean> = mutableStateOf(false)
    val deleted: Boolean get() = _deleted.value

    private val _data: MutableState<T> = mutableStateOf(fakeData)
    val data: T get() = _data.value

    fun onDeletedStateChange(deleted: Boolean) {
        if (deleted != _deleted.value) {
            _deleted.value = deleted
        }
    }

    fun onDataChanged(newData: T) {
        _data.value = newData
    }
}