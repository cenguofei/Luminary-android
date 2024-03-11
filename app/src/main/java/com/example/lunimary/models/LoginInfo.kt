package com.example.lunimary.models

@kotlinx.serialization.Serializable
data class LoginInfo(
    val isLogin: Boolean,
    val tokenExpired: Boolean
)