package com.example.lunimary.base

import com.example.lunimary.util.unknownErrorMsg

sealed interface DataState {

    object None : DataState

    class Success(val message: String) : DataState

    class Failed(private val e: Throwable) : DataState {
        val message: String get() = e.message ?: unknownErrorMsg
    }
}