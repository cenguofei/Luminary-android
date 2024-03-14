package com.example.lunimary.storage

import com.example.lunimary.models.User
import com.example.lunimary.models.checkUserNotNone
import com.example.lunimary.models.ktor.httpClient
import com.example.lunimary.models.ktor.setSession
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.util.currentUser
import com.example.lunimary.util.decodeParcelable
import com.example.lunimary.util.empty
import com.example.lunimary.util.encodeParcelable
import com.example.lunimary.util.failed
import com.example.lunimary.util.logd
import com.example.lunimary.util.refreshToken
import com.tencent.mmkv.MMKV
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun loadLocalToken(): BearerTokens? {
    if (currentUser == User.NONE) {
        return null
    }
    return decodeParcelable<TokenInfo>(
        key = MMKVKeys.TOKEN_INFO,
        mmapID = currentUser.username
    ).let {
        if (it == null) null
        else BearerTokens(
            accessToken = it.accessToken,
            refreshToken = it.refreshToken ?: empty
        ).also { tokens -> tokens.toString().logd() }
    }
}

fun removeToken() {
    val mmkv = MMKV.mmkvWithID(currentUser.username)
    mmkv.remove(MMKVKeys.TOKEN_INFO)
}
suspend fun refreshToken(): BearerTokens? {
    return withContext(Dispatchers.IO) {
        val rawResponse = httpClient.get(
            urlString = refreshToken
        ) {
            header("lunimary_token", "${loadLocalToken()?.refreshToken}")
            setSession()
        }
        if (rawResponse.status.failed()) {
            return@withContext null
        }
        val response = rawResponse.body<DataResponse<TokenInfo>>()
        response.data?.let {
            saveTokens(it,it.username)
        }
        val tokenInfo = response.data
        if (tokenInfo == null) null
        else BearerTokens(
            accessToken = tokenInfo.accessToken,
            refreshToken = tokenInfo.refreshToken ?: empty
        )
    }
}

fun saveTokens(tokenInfo: TokenInfo, username: String) {
    tokenInfo.encodeParcelable(
        key = MMKVKeys.TOKEN_INFO,
        mmapID = username
    ).also { tokens -> tokens.toString().logd("token") }
}