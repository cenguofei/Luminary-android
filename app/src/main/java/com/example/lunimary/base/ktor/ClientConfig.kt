package com.example.lunimary.base.ktor

import com.example.lunimary.util.logd
import com.example.lunimary.util.loge
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import java.util.concurrent.TimeUnit

const val HOST = "192.168.31.238"
const val PORT = 8080

val httpClient = HttpClient(OkHttp) {
    engine {
        config {
            connectTimeout(10L, TimeUnit.SECONDS)
            readTimeout(10L, TimeUnit.SECONDS)
            followRedirects(true)
        }
        addInterceptor(AuthInterceptor())
        addInterceptor(NetworkInterceptor())
    }
    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.ALL
        filter { request ->
            request.url.host.contains("luminary.blog")
        }
    }

    install(DefaultRequest)
    defaultRequest {
        url {
            protocol = URLProtocol.HTTP
            host = HOST
            port = PORT
        }
    }

    install(LunimaryHttpRequestRetry) {
        //在从服务器收到 5xx 响应时重试请求并指定重试次数
        retryOnServerErrors(maxRetries = 3)
        exponentialDelay()

        retryIf { _, response ->
            //当请求失败后最多重试三次
            (response.status == HttpStatusCode.InternalServerError) && retryCount <= 3
        }

        retryOnExceptionIf { _ , exception: Throwable ->
            "retryOnExceptionIf exception:${exception.message}".logd("http_request_retry")
            false
        }

        // Before retry
        modifyRequest { request ->
            request.headers.append("x-retry-count", retryCount.toString())
            "x-retry-count:$retryCount".logd("http_request_retry")
        }
    }

    install(Auth) {
        basic {
            realm = "Access to the '/' path"
        }
        bearer {
            // Load tokens from a local storage and return them as the 'BearerTokens' instance
            //loadTokens { loadLocalToken() }
            // Load tokens... Specify how to obtain a new token if the old one is invalid
            //refreshTokens { loadRemoteToken() }
        }
    }

    install(ContentNegotiation) {
        json()
    }

}.apply {
    //monitor and retry HTTP calls depending on a response.
    plugin(HttpSend).intercept { request ->
        val originalCall = execute(request)
        if (!originalCall.response.status.isSuccess()) {
            """
                Response Error:
                ${originalCall.request.url}, ${originalCall.response.status}
            """.trimIndent().loge()
        }
        originalCall
    }
}