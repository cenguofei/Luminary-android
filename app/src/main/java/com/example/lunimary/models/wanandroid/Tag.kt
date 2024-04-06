package com.example.lunimary.models.wanandroid

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val name: String,
    val url: String
)