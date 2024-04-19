package com.example.lunimary.model.source.remote.impl

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.lunimary.base.ktor.addPathParam
import com.example.lunimary.base.ktor.init
import com.example.lunimary.base.ktor.securityGet
import com.example.lunimary.base.ktor.securityPost
import com.example.lunimary.base.ktor.securityPut
import com.example.lunimary.base.ktor.setJsonBody
import com.example.lunimary.base.storage.MMKVKeys
import com.example.lunimary.base.storage.TokenInfo
import com.example.lunimary.base.storage.loadLocalToken
import com.example.lunimary.base.storage.saveSession
import com.example.lunimary.base.storage.saveTokens
import com.example.lunimary.model.LoginInfo
import com.example.lunimary.model.User
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.responses.UserResponse
import com.example.lunimary.model.source.remote.UserSource
import com.example.lunimary.util.checkIsLoginPath
import com.example.lunimary.util.empty
import com.example.lunimary.util.getUserPath
import com.example.lunimary.util.logd
import com.example.lunimary.util.loginPath
import com.example.lunimary.util.logoutPath
import com.example.lunimary.util.registerPath
import com.example.lunimary.util.updateUserPath
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.http.appendPathSegments
import io.ktor.http.parameters

class UserSourceImpl: BaseSourceImpl by BaseSourceImpl(),  UserSource {
    override suspend fun login(username: String, password: String): UserResponse {
        val response = client.submitForm(
            url = loginPath,
            formParameters = parameters {
                append("username", username)
                append("password", password)
            }
        )
        val session = response.headers[MMKVKeys.LUMINARY_SESSION_KEY]
            ?: empty.also { "Session is empty".logd() }
        val accessToken = response.headers["access_token"]
            ?: empty.also { "Access token is empty".logd() }
        val refreshToken = response.headers["refresh_token"]
            ?: empty.also { "Refresh token is empty".logd() }
        "session=$session \n accessToken=$accessToken \n refreshToken=$refreshToken".logd("token")
        val loginUsername = response.body<UserResponse>().data?.user?.username
        if (loginUsername != null) {
            "save token&session loginUsername=$loginUsername".logd()
            saveSession(loginUsername, session)
            saveTokens(TokenInfo(loginUsername, accessToken, refreshToken), loginUsername)
        }
        return response.body<UserResponse>().init(response)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun checkIsLogin(): DataResponse<LoginInfo> {
        return client.securityPost(
            urlString = checkIsLoginPath,
            needAuth = false
        ) {
            header("lunimary_token", "${loadLocalToken()?.accessToken}")
        }.init()
    }

    override suspend fun logout(): DataResponse<Unit> {
        return client.securityPost(logoutPath).init()
    }

    override suspend fun register(username: String, password: String): UserResponse {
        return client.submitForm(
            url = registerPath,
            formParameters = parameters {
                append("username", username)
                append("password", password)
            }
        ).init()
    }

    override suspend fun queryUser(userId: Long): UserResponse {
        val response = client.securityGet(urlString = getUserPath) {
            url { appendPathSegments(userId.toString()) }
        }
        return response.init()
    }

    override suspend fun update(user: User): UserResponse {
        return client.securityPut(urlString = updateUserPath) {
            addPathParam(user.id)
            setJsonBody(user)
        }.init()
    }
}