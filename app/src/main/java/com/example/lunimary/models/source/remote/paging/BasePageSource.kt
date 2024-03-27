package com.example.lunimary.models.source.remote.paging

import com.example.lunimary.models.ktor.httpClient
import io.ktor.client.HttpClient

interface BasePageSource {
    val client: HttpClient
}

fun BasePageSource(): BasePageSource {
    return object : BasePageSource {
        override val client: HttpClient
            get() = httpClient
    }
}