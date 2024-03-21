package com.example.lunimary.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

interface BaseRepository {
    val dispatcher: CoroutineDispatcher

    suspend fun <T> withDispatcher(
        block: suspend CoroutineScope.() -> T
    ): T { return kotlinx.coroutines.withContext(dispatcher, block) }
}

fun BaseRepository(): BaseRepository {
    return object : BaseRepository {
        override val dispatcher: CoroutineDispatcher
            get() = Dispatchers.IO
    }
}