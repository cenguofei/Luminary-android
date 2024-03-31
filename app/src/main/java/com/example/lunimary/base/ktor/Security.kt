package com.example.lunimary.base.ktor

import com.example.lunimary.models.User
import com.example.lunimary.base.storage.MMKVKeys
import com.example.lunimary.base.storage.loadLocalToken
import com.example.lunimary.base.storage.loadSession
import com.example.lunimary.base.currentUser
import com.example.lunimary.util.logd
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.headers
import io.ktor.http.HttpMessageBuilder

fun HttpMessageBuilder.setSession() {
    checkUser {
        headers {
            append(
                name = MMKVKeys.LUMINARY_SESSION,
                value = loadSession(MMKVKeys.LUMINARY_SESSION).also {
                    "session=$it".logd()
                }
            )
        }
    }
}

fun HttpMessageBuilder.setBearAuth() {
    "begin set token".logd()
    checkUser {
        loadLocalToken()?.accessToken?.let {
            "set token: $it".logd("token_debug")
            bearerAuth(it)
        }
    }
}

inline fun checkUser(action: () -> Unit) {
    "checkUser:currentUser=$currentUser".logd()
    if (currentUser != User.NONE) {
        action()
    }
}