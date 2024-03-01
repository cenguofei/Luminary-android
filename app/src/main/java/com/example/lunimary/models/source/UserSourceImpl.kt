package com.example.lunimary.models.source

import com.example.lunimary.base.BaseResponse
import com.example.lunimary.models.ktor.httpClient
import com.example.lunimary.models.ktor.securityGet
import com.example.lunimary.models.ktor.securityPost
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.UserResponse
import com.example.lunimary.storage.MMKVKeys
import com.example.lunimary.storage.TokenInfo
import com.example.lunimary.storage.saveSession
import com.example.lunimary.storage.saveTokens
import com.example.lunimary.util.checkIsLoginPath
import com.example.lunimary.util.currentUser
import com.example.lunimary.util.empty
import com.example.lunimary.util.getUserPath
import com.example.lunimary.util.logd
import com.example.lunimary.util.loginPath
import com.example.lunimary.util.logoutPath
import com.example.lunimary.util.registerPath
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters

class UserSourceImpl(private val client: HttpClient = httpClient) : UserSource {
    override suspend fun login(username: String, password: String): UserResponse {
        val response = client.submitForm(
            url = loginPath,
            formParameters = parameters {
                append("username", username)
                append("password", password)
            }
        )
        val session = response.headers[MMKVKeys.LUMINARY_SESSION]
            ?: empty.also { "Session is empty".logd() }
        val accessToken = response.headers[MMKVKeys.ACCESS_TOKEN]
            ?: empty.also { "Access token is empty".logd() }
        val refreshToken = response.headers[MMKVKeys.REFRESH_TOKEN]
            ?: empty.also { "Refresh token is empty".logd() }
        "session=$session \n accessToken=$accessToken \n refreshToken=$refreshToken".logd("token")
        val loginUsername = response.body<UserResponse>().data?.user?.username
        if (loginUsername != null) {
            "save token&session".logd()
            saveSession(loginUsername, session)
            saveTokens(TokenInfo(loginUsername, accessToken, refreshToken), loginUsername)
        }
        return response.body<UserResponse>().init(response)
    }

    override suspend fun checkIsLogin(): DataResponse<Boolean> {
        return client.securityPost(
            urlString = checkIsLoginPath,
            needAuth = false
        ).let { it.body<DataResponse<Boolean>>().init(it) }
    }

    override suspend fun logout(): DataResponse<Unit> {
        return client.securityPost(logoutPath).let {
            it.body<DataResponse<Unit>>().init(it)
        }
    }

    override suspend fun register(username: String, password: String): UserResponse {
        return client.submitForm(
            url = registerPath,
            formParameters = parameters {
                append("username", username)
                append("password", password)
            }
        ).let { it.body<UserResponse>().init(it) }
    }

    override suspend fun queryUser(id: Long): UserResponse {
        val response = client.securityGet(urlString = getUserPath) {
            url {
                parameters.append("id", id.toString())
            }
        }
        return response.body<UserResponse>().init(response)
    }
}