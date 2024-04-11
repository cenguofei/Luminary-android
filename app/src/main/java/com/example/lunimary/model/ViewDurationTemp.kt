package com.example.lunimary.model

import com.example.lunimary.util.Default

/**
 * 用户浏览文章时长类，用于推荐文章
 */
@kotlinx.serialization.Serializable
data class ViewDurationTemp(
    val tags: List<String> = emptyList(),
    val userId: Long = Long.Default,
    val duration: Long = Long.Default,
    val timestamp: Long = Long.Default
)