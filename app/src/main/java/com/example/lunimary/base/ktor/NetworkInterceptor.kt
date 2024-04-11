package com.example.lunimary.base.ktor

import com.example.lunimary.LunimaryApplication.Companion.applicationContext
import com.example.lunimary.base.network.isCurrentlyConnected
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.util.empty
import com.example.lunimary.util.logd
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class NetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val app = applicationContext
        val connected = app.isCurrentlyConnected()
        "network interceptor connected:$connected".logd("NetworkInterceptor")
        if (!connected) {
            "network interceptor 返回自定义Response".logd("NetworkInterceptor")
            val newRequest = request.newBuilder()
                .build()
            return buildNetworkErrorResponse(newRequest)
        }
        return chain.proceed(request)
    }
}

const val LUNIMARY_NO_NETWORK_CODE = 13141024
const val NO_NET_HEADER_KEY = "no_network"

fun buildNetworkErrorResponse(request: Request): Response {
    return Response.Builder()
        .request(request)
        .code(LUNIMARY_NO_NETWORK_CODE)
        .header(NO_NET_HEADER_KEY, "true")
        .message(empty)
        .protocol(Protocol.HTTP_1_1)
        .header("Content-Type", "application/json")
        .header("Network-Status", "off")
        .body(
            Json.encodeToString(DataResponse<Unit>())
                .toResponseBody("application/json".toMediaType())
        )
        .build()
}