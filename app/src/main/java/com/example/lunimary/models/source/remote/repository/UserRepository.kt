package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.BaseRepository
import com.example.lunimary.models.LoginInfo
import com.example.lunimary.models.User
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.UserResponse
import com.example.lunimary.models.source.remote.UserSource
import com.example.lunimary.models.source.remote.impl.UserSourceImpl

class UserRepository : BaseRepository by BaseRepository(), UserSource {
    private val userSource: UserSource = UserSourceImpl()
    override suspend fun login(username: String, password: String): UserResponse =
        withDispatcher { userSource.login(username, password) }

    override suspend fun checkIsLogin(): DataResponse<LoginInfo> =
        withDispatcher { userSource.checkIsLogin() }

    override suspend fun logout(): DataResponse<Unit> =
        withDispatcher { userSource.logout() }

    override suspend fun register(username: String, password: String): UserResponse =
        withDispatcher { userSource.register(username, password) }

    override suspend fun queryUser(userId: Long): UserResponse =
        withDispatcher { userSource.queryUser(userId) }

    override suspend fun update(user: User): UserResponse =
        withDispatcher { userSource.update(user) }
}