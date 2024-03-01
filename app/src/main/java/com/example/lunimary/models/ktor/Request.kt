package com.example.lunimary.models.ktor

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import java.nio.file.Files.delete

suspend inline fun HttpClient.securityGet(
    urlString: String,
    needSession: Boolean = true,
    needAuth: Boolean = true,
    noinline block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = get(
    addSecurityFactors(
        urlString = urlString,
        needSession = needSession,
        needAuth = needAuth,
        block = block
    )
)

suspend inline fun HttpClient.securityPost(
    urlString: String,
    needSession: Boolean = true,
    needAuth: Boolean = true,
    noinline block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = post(
    addSecurityFactors(
        urlString = urlString,
        needSession = needSession,
        needAuth = needAuth,
        block = block
    )
)

suspend inline fun HttpClient.securityDelete(
    urlString: String,
    needSession: Boolean = true,
    needAuth: Boolean = true,
    noinline block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = delete(
    addSecurityFactors(
        urlString = urlString,
        needSession = needSession,
        needAuth = needAuth,
        block = block
    )
)

suspend inline fun HttpClient.securityPut(
    urlString: String,
    needSession: Boolean = true,
    needAuth: Boolean = true,
    noinline block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = put(
    addSecurityFactors(
        urlString = urlString,
        needSession = needSession,
        needAuth = needAuth,
        block = block
    )
)

fun HttpRequestBuilder.addJson(obj: Any): HttpRequestBuilder {
    contentType(ContentType.Application.Json)
    setBody(obj)
    return this
}

fun HttpRequestBuilder.addPagesParam(curPage: Int, perPageCount: Int) {
    url {
        parameters.append("curPage", "$curPage")
        parameters.append("perPageCount", "$perPageCount")
    }
}

fun addSecurityFactors(
    urlString: String,
    needSession: Boolean = true,
    needAuth: Boolean = true,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpRequestBuilder = HttpRequestBuilder().apply {
    url(urlString)
    if (needSession) { setSession() }
    if (needAuth) { setBearAuth() }
}.apply(block)