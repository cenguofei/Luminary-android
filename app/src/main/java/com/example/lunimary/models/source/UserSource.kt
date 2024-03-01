package com.example.lunimary.models.source

import com.example.lunimary.base.BaseResponse
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
    suspend fun checkIsLogin() : DataResponse<Boolean>

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