package com.example.lunimary.base.network

import com.example.lunimary.util.empty

sealed class NetworkResult<T>(
    val msg: String? = empty,
    val data: T? = null
) {
    class Loading<T>: NetworkResult<T>()

    class Success<T>(data: T? = null, msg: String? = empty): NetworkResult<T>(data = data, msg = msg)

    class Error<T>(msg: String = empty): NetworkResult<T>(msg = msg)

    class None<T>: NetworkResult<T>()
}
fun <T> NetworkResult<T>?.isError(): Boolean = this != null && this == NetworkResult.Error<T>()

fun <T> NetworkResult<T>?.asSuccess(): NetworkResult.Success<T>? {
    return this as? NetworkResult.Success<T>
}

fun <T> NetworkResult<T>?.asError(): NetworkResult.Error<T>? {
    return this as? NetworkResult.Error<T>
}

