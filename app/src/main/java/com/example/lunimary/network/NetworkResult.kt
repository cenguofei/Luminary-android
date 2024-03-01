package com.example.lunimary.network

import com.example.lunimary.design.SnackbarData
import com.example.lunimary.util.empty
import com.example.lunimary.util.logd

sealed class NetworkResult<T>(
    val msg: String = empty,
    val data: T? = null
) {
    class Loading<T>: NetworkResult<T>()

    class Success<T>(data: T? = null): NetworkResult<T>(data = data)

    class Error<T>(msg: String = empty): NetworkResult<T>(msg = msg)

    class None<T>: NetworkResult<T>()
}

fun <T> NetworkResult<T>?.isLoading(): Boolean = run {
    "$this".logd("networkResult")
    this != null && this == NetworkResult.Loading<T>()
}

fun <T> NetworkResult<T>?.isSuccess(): Boolean = this != null && this == NetworkResult.Success<T>()

fun <T> NetworkResult<T>?.isError(): Boolean = this != null && this == NetworkResult.Error<T>()

fun <T> NetworkResult<T>?.asSuccess(): NetworkResult.Success<T>? {
    return this as? NetworkResult.Success<T>
}

fun <T> NetworkResult<T>?.asError(): NetworkResult.Error<T>? {
    return this as? NetworkResult.Error<T>
}

fun <T> NetworkResult<T>?.getErrorMsg(): String {
    return this?.asError()?.msg ?: empty
}

fun <T> NetworkResult<T>?.toSnackbarData(): SnackbarData? {
    return if (this.isError()) {
        this!!.asError()?.msg?.let { SnackbarData(it) }
    } else null
}