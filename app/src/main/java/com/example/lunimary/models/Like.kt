package com.example.lunimary.models

import com.example.lunimary.util.Default

@kotlinx.serialization.Serializable
data class Like(
    val id: Long = Long.Default,

    /**
     * User ID who likes this article
     */
    val userId: Long = Long.Default,

    /**
     * ID of article
     */
    val articleId: Long = Long.Default,

    /**
     * Like time
     */
    val timestamp: Long = Long.Default
)