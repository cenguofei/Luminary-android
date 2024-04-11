package com.example.lunimary.model.source.remote

import com.example.lunimary.model.LoginInfo
import com.example.lunimary.model.User
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.responses.UserResponse
import com.example.lunimary.model.source.remote.impl.UserSourceImpl

interface UserSource {
    /**
     * 登录
     */
    suspend fun login(
        username: String,
        password: String
    ) : UserResponse

    /**
     * 检查是否有登录状态
     */
    suspend fun checkIsLogin() : DataResponse<LoginInfo>

    /**
     * 退出登录
     */
    suspend fun logout() : DataResponse<Unit>

    /**
     * 注册
     */
    suspend fun register(
        username: String,
        password: String
    ) : UserResponse

    /**
     * 查询用户信息
     */
    suspend fun queryUser(userId: Long) : UserResponse

    suspend fun update(user: User) : UserResponse

    companion object : UserSource by UserSourceImpl()
}