package com.example.lunimary.base.viewmodel

import com.example.lunimary.base.BaseResponse
import kotlinx.coroutines.Job

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

    fun <T> request(
        onSuccess: (data: T?, msg: String?) -> Unit = { _, _ -> },
        emptySuccess: () -> Unit = {},
        onFailed: (msg: String) -> Unit = {},
        onFinish: () -> Unit = {},
        block: suspend () -> BaseResponse<T>
    ): Job
}