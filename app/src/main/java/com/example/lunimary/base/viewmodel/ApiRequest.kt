package com.example.lunimary.base.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.lunimary.base.BaseResponse
import com.example.lunimary.util.logd
import com.example.lunimary.util.loge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * 处理网络请求
 */
interface ApiRequest {
    /**
     * 开始请求时调用，前一个调用没返回之前后续调用无效
     */
    fun fly(url: String, action: () -> Unit)

    /**
     * 请求完成时调用
     */
    fun land(url: String)

    fun <T> BaseViewModel.request(
        onSuccess: (data: T?, msg: String?) -> Unit = { _, _ -> },
        emptySuccess: () -> Unit = {},
        onFailed: (msg: String) -> Unit = {},
        onFinish: () -> Unit = {},
        coroutineScope: CoroutineScope = viewModelScope,
        block: suspend CoroutineScope.() -> BaseResponse<T>
    ): Job
}

@Suppress("UNCHECKED_CAST")
fun <T1, T2> BaseViewModel.parallelRequests(
    onSuccess: (T1, T2) -> Unit = {_, _ -> },
    onFailed: (msg: String) -> Unit = {},
    onFinish: () -> Unit = {},
    block1: ParallelRequestBlockType<T1>,
    block2: ParallelRequestBlockType<T2>,
): Job {
    return viewModelScope.launch {
        runCatching {
            val deffer1 = async { block1.invoke() }
            val deffer2 = async { block2.invoke() }
            val result = mutableListOf<BaseResponse<Any>>()
            result += (deffer1.await() as BaseResponse<Any>)
            result += (deffer2.await() as BaseResponse<Any>)
            result
        }.onSuccess { responses ->
            runCatching {
                val successData = mutableListOf<Any?>()
                responses.forEach { response ->
                    if (response.isSuccess()) {
                        successData += response.data
                        response.data ?: run {
                            "data is null.".logd()
                        }
                    } else {
                        onFailed(response.msg)
                        return@launch
                    }
                }
                onSuccess(successData[0] as T1, successData[1] as T2)
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

typealias ParallelRequestBlockType<T> = suspend () -> BaseResponse<T>