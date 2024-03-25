package com.example.lunimary.models.ktor

import com.example.lunimary.storage.TokenInfo
import com.example.lunimary.util.loge
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.forms.submitForm
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.formUrlEncode
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
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
        //addNetworkInterceptor(interceptor)
        //preconfigured = okHttpClientInstance
    }
    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.ALL
        filter { request ->
            request.url.host.contains("luminary.blog")
        }
//        sanitizeHeader { header ->
//            header == HttpHeaders.Authorization
//        }
    }

    install(DefaultRequest)
    defaultRequest {
        url {
            protocol = URLProtocol.HTTP
            host = HOST
            port = PORT
        }
    }

    install(HttpRequestRetry) {
        //在从服务器收到 5xx 响应时重试请求并指定重试次数
        retryOnServerErrors(maxRetries = 3)
        exponentialDelay()

        retryIf { _, response ->
            //当请求失败后最多重试三次
            (response.status == HttpStatusCode.InternalServerError) && retryCount <= 3
        }

        // Before retry
        modifyRequest { request ->
            request.headers.append("x-retry-count", retryCount.toString())
        }
    }

    //cache
    /*install(HttpCache) {
        val cacheFile = Files.createDirectories(Paths.get("build/cache")).toFile()
        publicStorage(FileStorage(cacheFile))
    }*/

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
                ${originalCall.request}, ${originalCall.request.headers},
                ${originalCall.response.status}
            """.trimIndent().loge()
        }
        originalCall
    }
}
fun main() {
    runBlocking {
        // Step 1: Get an authorization code
        val authorizationUrlQuery = parameters {
            append("client_id", System.getenv("GOOGLE_CLIENT_ID"))
            append("scope", "https://www.googleapis.com/auth/userinfo.profile")
            append("response_type", "code")
            append("redirect_uri", "http://127.0.0.1:8080")
            append("access_type", "offline")
        }.formUrlEncode()
        println("https://accounts.google.com/o/oauth2/auth?$authorizationUrlQuery")
        println("Open a link above, get the authorization code, insert it below, and press Enter.")
        val authorizationCode = readln()

        // Step 2: Create a storage for tokens
        val bearerTokenStorage = mutableListOf<BearerTokens>()

        // Step 3: Configure the client for receiving tokens and accessing the protected API
        val client = HttpClient(OkHttp) {

            install(Auth) {
                bearer {
                    loadTokens {
                        bearerTokenStorage.last()
                    }
                    refreshTokens {
                        val refreshTokenInfo: TokenInfo = client.submitForm(
                            url = "https://accounts.google.com/o/oauth2/token",
                            formParameters = parameters {
                                append("grant_type", "refresh_token")
                                append("client_id", System.getenv("GOOGLE_CLIENT_ID"))
                                append("refresh_token", oldTokens?.refreshToken ?: "")
                            }
                        ) { markAsRefreshTokenRequest() }.body()
                        bearerTokenStorage.add(
                            BearerTokens(
                                refreshTokenInfo.accessToken,
                                oldTokens?.refreshToken!!
                            )
                        )
                        bearerTokenStorage.last()
                    }
                    sendWithoutRequest { request ->
                        request.url.host == "www.googleapis.com"
                    }
                }
            }
        }

        // Step 4: Exchange the authorization code for tokens and save tokens in the storage
        val tokenInfo: TokenInfo = client.submitForm(
            url = "https://accounts.google.com/o/oauth2/token",
            formParameters = parameters {
                append("grant_type", "authorization_code")
                append("code", authorizationCode)
                append("client_id", System.getenv("GOOGLE_CLIENT_ID"))
                append("client_secret", System.getenv("GOOGLE_CLIENT_SECRET"))
                append("redirect_uri", "http://127.0.0.1:8080")
            }
        ).body()
        bearerTokenStorage.add(BearerTokens(tokenInfo.accessToken, tokenInfo.refreshToken!!))
    }
}