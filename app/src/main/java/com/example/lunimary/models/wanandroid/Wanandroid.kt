package com.example.lunimary.models.wanandroid

import kotlinx.serialization.Serializable

@Serializable
data class Wanandroid(
    val data: Data,
    val errorCode: Int,
    val errorMsg: String
)