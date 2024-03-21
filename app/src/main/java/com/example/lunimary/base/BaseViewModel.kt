package com.example.lunimary.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunimary.util.logd
import com.example.lunimary.util.loge
import io.ktor.util.collections.ConcurrentSet
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val processingMap = ConcurrentSet<String>()
    fun fly(url: String, action: () -> Unit) {
        if (url !in processingMap) {
            action()
        }
    }

    fun land(url: String) {
        if (url in processingMap) {
            processingMap.remove(url)
        }
    }
}

fun <T> BaseViewModel.request(
    block: suspend () -> BaseResponse<T>,
    onSuccess: (data:T?, msg: String?) -> Unit = { _, _ -> },
    onFailed: (msg: String) -> Unit = {},
    onFinish: () -> Unit = {}
): Job {
    return viewModelScope.launch {
        runCatching {
            block()
        }.onSuccess { response ->
            "request success: response=$response".logd()
            runCatching {
                if (response.isSuccess()) {
                    onSuccess(response.data, response.msg)
                    response.data ?: run {
                        "data is null.".logd()
                    }
                } else {
                    onFailed(response.msg)
                }
            }
            onFinish()
        }.onFailure {
            it.message?.loge()
            it.printStackTrace()
            it.message?.let { msg -> onFailed(msg) }
            onFinish()
        }
    }
}