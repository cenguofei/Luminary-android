package com.example.lunimary.models.ktor

import com.example.lunimary.storage.refreshToken
import com.example.lunimary.util.HttpConst
import com.example.lunimary.util.UserState
import com.example.lunimary.util.logd
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        "request headers:${request.headers}".logd()
        return if (response.code == HttpStatusCode.Unauthorized.value) {
            "intercept Unauthorized: response=$response".logd("token")
            runBlocking(context = Dispatchers.IO) {
                val deffer = async { refreshToken() }
                val tokens = deffer.await()
                "refresh token newTokens={access:${tokens?.accessToken}, refresh:${tokens?.refreshToken}}".logd("token")
                if (tokens == null) {
                    "tokens == null, 返回原来的".logd("token")
                    UserState.updateLoginState(false, "clientConfig")
                    return@runBlocking response.newBuilder()
                        .apply {
                            code(HttpStatusCode.Conflict.value)
                            message("error occur, refresh token failed.")
                            header(HttpConst.NEED_LOGIN.first, HttpConst.NEED_LOGIN.second)
                        }
                        .build()
                }
                response.close()
                "refresh token success.".logd("token")
                val newRequest = request.newBuilder()
                newRequest.header(HttpHeaders.Authorization, "Bearer ${tokens.accessToken}")
                chain.proceed(newRequest.build())
            }
        } else {
            response
        }
    }
}