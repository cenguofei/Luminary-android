package com.example.lunimary.base.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lunimary.base.BaseResponse
import com.example.lunimary.util.logd
import com.example.lunimary.util.loge
import io.ktor.util.collections.ConcurrentSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class ApiViewModel : ViewModel(), ApiRequest {
    private val processingMap = ConcurrentSet<String>()
    override fun fly(url: String, action: () -> Unit) {
        if (url !in processingMap) {
            "fly $url".logd("fly_request")
            processingMap += url
            action()
        }
    }

    override fun land(url: String) {
        if (url in processingMap) {
            processingMap.remove(url)
            "land $url".logd("fly_request")
        }
    }

    override fun <T> BaseViewModel.request(
        onSuccess: (data: T?, msg: String?) -> Unit,
        emptySuccess: () -> Unit,
        onFailed: (msg: String) -> Unit,
        onFinish: () -> Unit,
        coroutineScope: CoroutineScope,
        block: suspend CoroutineScope.() -> BaseResponse<T>
    ): Job {
        return coroutineScope.launch {
            runCatching {
                block()
            }.onSuccess { response ->
                "request success: response=$response".logd()
                runCatching {
                    if (response.isSuccess()) {
                        onSuccess(response.data, response.msg)
                        emptySuccess()
                        response.data ?: run {
                            "data is null.".logd()
                        }
                    } else {
                        onFailed(response.msg)
                    }
                }.onFailure {
                    onFailed(it.errorMsg)
                }
                onFinish()
            }.onFailure {
                it.message?.loge()
                it.printStackTrace()
                onFailed(it.errorMsg)
                onFinish()
            }
        }
    }
}