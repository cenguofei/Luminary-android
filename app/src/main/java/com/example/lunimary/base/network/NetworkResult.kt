package com.example.lunimary.base.network

import com.example.lunimary.util.empty

/**
 * 网络请求结果
 */
sealed class NetworkResult<T>(
    val msg: String? = empty,
    val data: T? = null
) {
    /**
     * 请求中
     */
    class Loading<T>: NetworkResult<T>()

    /**
     * 成功
     */
    class Success<T>(data: T? = null, msg: String? = empty): NetworkResult<T>(data = data, msg = msg)

    /**
     * 失败
     */
    class Error<T>(msg: String = empty): NetworkResult<T>(msg = msg)

    /**
     * 初始状态
     */
    class None<T>: NetworkResult<T>()
}
fun <T> NetworkResult<T>?.isError(): Boolean = this != null && this == NetworkResult.Error<T>()

fun <T> NetworkResult<T>?.asSuccess(): NetworkResult.Success<T>? {
    return this as? NetworkResult.Success<T>
}

fun <T> NetworkResult<T>?.asError(): NetworkResult.Error<T>? {
    return this as? NetworkResult.Error<T>
}

