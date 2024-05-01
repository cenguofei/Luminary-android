package com.example.lunimary.base.ktor

import com.example.lunimary.base.currentUser
import com.example.lunimary.base.storage.loadLocalToken
import com.example.lunimary.base.storage.loadSession
import com.example.lunimary.model.User
import com.example.lunimary.util.logd
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.headers
import io.ktor.http.HttpMessageBuilder

fun HttpMessageBuilder.setSession() {
    checkUser {
        headers {
            append(
                name = "LUMINARY_SESSION",
                value = loadSession().also {
                    "session=$it, username=${currentUser.username}".logd("security")
                }
            )
        }
    }
}

fun HttpMessageBuilder.setBearAuth() {
    checkUser {
        loadLocalToken()?.accessToken?.let {
            "loadLocalToken: $it, username=${currentUser.username}".logd("security")
            bearerAuth(it)
        }
    }
}

inline fun checkUser(action: () -> Unit) {
    if (currentUser != User.NONE) {
        action()
    }
}