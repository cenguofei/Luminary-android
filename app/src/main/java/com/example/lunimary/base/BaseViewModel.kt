package com.example.lunimary.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunimary.util.logd
import com.example.lunimary.util.loge
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel()

fun <T> BaseViewModel.request(
    block: suspend () -> BaseResponse<T>,
    onSuccess: (T?) -> Unit,
    onFailed: (msg: String) -> Unit = {},
): Job {
    return viewModelScope.launch {
        runCatching {
            block()
        }.onSuccess { response ->
            runCatching {
                if (response.isSuccess()) {
                    onSuccess(response.data)
                    response.data ?: run {
                        "data is null.".logd()
                    }
                } else {
                    onFailed(response.msg)
                }
            }
        }.onFailure {
            it.message?.loge()
            it.printStackTrace()
            it.message?.let { msg -> onFailed(msg) }
        }
    }
}