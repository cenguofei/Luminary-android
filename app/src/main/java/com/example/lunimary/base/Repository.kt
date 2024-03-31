package com.example.lunimary.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface Repository {
    val dispatcher: CoroutineDispatcher

    suspend fun <T> withDispatcher(
        block: suspend CoroutineScope.() -> T
    ): T = withContext(dispatcher, block)
}

fun Repository(): Repository {
    return object : Repository {
        override val dispatcher: CoroutineDispatcher
            get() = Dispatchers.IO
    }
}