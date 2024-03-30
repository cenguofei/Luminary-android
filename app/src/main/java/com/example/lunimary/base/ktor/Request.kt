package com.example.lunimary.base.ktor

import com.example.lunimary.base.BaseResponse
import com.example.lunimary.base.currentUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType

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

fun HttpRequestBuilder.setJsonBody(obj: Any): HttpRequestBuilder {
    contentType(ContentType.Application.Json)
    setBody(obj)
    return this
}

fun HttpRequestBuilder.addPagesParam(curPage: Int, perPageCount: Int) {
    url {
        parameters.append("wishPage", "$curPage")
        parameters.append("perPageCount", "$perPageCount")
    }
}

fun HttpRequestBuilder.addQueryParam(name: String, value: Any) {
    url {
        parameters.append(name, value.toString())
    }
}

fun HttpRequestBuilder.addUserIdPath(
    userId: Long? = null
) {
    val id = userId ?: currentUser.id
    addPathParam(id)
}

fun HttpRequestBuilder.addPathParam(value: Any) {
    url { appendPathSegments(value.toString()) }
}

fun addSecurityFactors(
    urlString: String,
    needSession: Boolean = true,
    needAuth: Boolean = true,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpRequestBuilder = HttpRequestBuilder().apply {
    url(urlString)
    if (needSession) {
        setSession()
    }
    if (needAuth) {
        setBearAuth()
    }
}.apply(block)


suspend inline fun <T, reified R : BaseResponse<T>> HttpResponse.init(): R {
    return body<R>().init(this)
}