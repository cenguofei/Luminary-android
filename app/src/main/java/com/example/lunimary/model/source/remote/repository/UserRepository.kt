package com.example.lunimary.model.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.model.LoginInfo
import com.example.lunimary.model.User
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.responses.UserResponse
import com.example.lunimary.model.source.remote.UserSource

class UserRepository : Repository by Repository() {
    private val userSource = UserSource
    suspend fun login(username: String, password: String): UserResponse =
        withDispatcher { userSource.login(username, password) }

    suspend fun checkIsLogin(): DataResponse<LoginInfo> =
        withDispatcher { userSource.checkIsLogin() }

    suspend fun logout(): DataResponse<Unit> =
        withDispatcher { userSource.logout() }

    suspend fun register(username: String, password: String): UserResponse =
        withDispatcher { userSource.register(username, password) }

    suspend fun queryUser(userId: Long): UserResponse =
        withDispatcher { userSource.queryUser(userId) }

    suspend fun update(user: User): UserResponse =
        withDispatcher { userSource.update(user) }
}