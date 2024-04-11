package com.example.lunimary.model.responses

import com.example.lunimary.base.BaseResponse
import com.example.lunimary.model.User
import kotlinx.serialization.Serializable

@Serializable
class UserResponse: BaseResponse<UserData>()

@Serializable
data class UserData(
    val shouldLogin: Boolean = false,
    val user: User? = null
)