package com.example.lunimary.models.responses

import com.example.lunimary.base.BaseResponse
import com.example.lunimary.models.User
import kotlinx.serialization.Serializable

@Serializable
class UserResponse: BaseResponse<UserData>()

@Serializable
data class UserData(
    val shouldLogin: Boolean = false,
    val user: User? = null
)