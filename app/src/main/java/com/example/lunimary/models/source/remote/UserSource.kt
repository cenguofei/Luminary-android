package com.example.lunimary.models.source.remote

import com.example.lunimary.base.BaseResponse
import com.example.lunimary.models.LoginInfo
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.UserResponse

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
    suspend fun queryUser(id: Long) : UserResponse
}