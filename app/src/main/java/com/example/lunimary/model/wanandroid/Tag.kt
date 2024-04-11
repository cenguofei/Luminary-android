package com.example.lunimary.model.wanandroid

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val name: String,
    val url: String
)