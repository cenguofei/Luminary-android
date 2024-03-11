package com.example.lunimary.ui.webview

import com.example.lunimary.util.empty
import io.ktor.util.collections.ConcurrentMap

object UrlCache {
    private val urlMap = mutableMapOf<Long, String>()

    fun putUrl(
        key: Long,
        url: String
    ) {
        urlMap[key] = url
    }

    fun getUrl(key: Long): String = urlMap[key] ?: DEFAULT_WEB_URL

    private const val DEFAULT_WEB_URL = "http://localhost:8080/index"
}