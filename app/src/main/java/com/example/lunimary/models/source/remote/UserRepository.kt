package com.example.lunimary.models.source.remote

import com.example.lunimary.base.BaseRepository
import com.example.lunimary.models.LoginInfo
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRepository(), UserSource {
    private val userSource: UserSource = UserSourceImpl()
    override suspend fun login(username: String, password: String): UserResponse =
        withContext(dispatcher) { userSource.login(username, password) }

    override suspend fun checkIsLogin(): DataResponse<LoginInfo> =
        withContext(dispatcher) { userSource.checkIsLogin() }

    override suspend fun logout(): DataResponse<Unit> =
        withContext(dispatcher) { userSource.logout() }

    override suspend fun register(username: String, password: String): UserResponse =
        withContext(dispatcher) { userSource.register(username, password) }

    override suspend fun queryUser(id: Long): UserResponse =
        withContext(dispatcher) { userSource.queryUser(id) }
}