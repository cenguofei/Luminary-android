package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.models.ktor.httpClient
import io.ktor.client.HttpClient
import okhttp3.OkHttpClient

interface BaseSourceImpl {
    val client: HttpClient
}

fun BaseSourceImpl(): BaseSourceImpl {
    return object : BaseSourceImpl {
        override val client: HttpClient
            get() = httpClient
    }
}