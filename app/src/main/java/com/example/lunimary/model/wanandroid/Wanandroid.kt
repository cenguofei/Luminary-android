package com.example.lunimary.model.wanandroid

import kotlinx.serialization.Serializable

@Serializable
data class Wanandroid(
    val data: Data,
    val errorCode: Int,
    val errorMsg: String
)