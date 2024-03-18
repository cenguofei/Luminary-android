package com.example.lunimary.base

import com.example.lunimary.models.ktor.httpClient
import com.example.lunimary.models.source.remote.impl.BaseSourceImpl
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

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