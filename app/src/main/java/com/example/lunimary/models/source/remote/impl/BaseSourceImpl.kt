package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.base.ktor.httpClient
import io.ktor.client.HttpClient

interface BaseSourceImpl {
    val client: HttpClient
}

fun BaseSourceImpl(): BaseSourceImpl {
    return object : BaseSourceImpl {
        override val client: HttpClient
            get() = httpClient
    }
}