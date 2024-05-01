package com.example.lunimary.base.storage

import com.example.lunimary.base.currentUser
import com.example.lunimary.base.ktor.httpClient
import com.example.lunimary.base.ktor.setSession
import com.example.lunimary.base.mmkv.decodeParcelable
import com.example.lunimary.base.mmkv.encodeParcelable
import com.example.lunimary.model.User
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.util.empty
import com.example.lunimary.util.failed
import com.example.lunimary.util.logd
import com.example.lunimary.util.refreshToken
import com.tencent.mmkv.MMKV
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun saveTokens(tokenInfo: TokenInfo, username: String) {
    "saveTokens, username=$username, accessToken=${tokenInfo.accessToken.length}".logd("cgf_security")
    tokenInfo.encodeParcelable(
        key = MMKVKeys.TOKEN_INFO_KEY,
        mmapID = username
    )
}

fun loadLocalToken(): BearerTokens? {
    if (currentUser == User.NONE) {
        "loadLocalToken currentUser = User.NONE".logd("cgf_security")
        return null
    }
    val username = currentUser.username
    return decodeParcelable<TokenInfo>(
        key = MMKVKeys.TOKEN_INFO_KEY,
        mmapID = username
    ).let {
        if (it == null) null
        else BearerTokens(
            accessToken = it.accessToken,
            refreshToken = it.refreshToken ?: empty
        ).also { tokens ->
            "loadLocalToken, username=$username, tokens=${tokens.accessToken.length}, ${tokens.refreshToken.length}".logd(
                "cgf_security"
            )
        }
    }
}

fun removeToken() {
    "remove token".logd("cgf_security")
    val mmkv = MMKV.mmkvWithID(currentUser.username)
    mmkv.remove(MMKVKeys.TOKEN_INFO_KEY)
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
            saveTokens(it, it.username)
        }
        val tokenInfo = response.data
        if (tokenInfo == null) null
        else BearerTokens(
            accessToken = tokenInfo.accessToken,
            refreshToken = tokenInfo.refreshToken ?: empty
        )
    }
}