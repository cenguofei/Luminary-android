package com.example.lunimary.model

@kotlinx.serialization.Serializable
data class LoginInfo(
    val isLogin: Boolean,
    val tokenExpired: Boolean
)