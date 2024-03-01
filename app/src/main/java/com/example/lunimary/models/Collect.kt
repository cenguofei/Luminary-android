package com.example.lunimary.models

import com.example.lunimary.util.Default
import kotlinx.serialization.Serializable

@Serializable
data class Collect(
    val id: Long = Long.Default,
    /**
     * User ID for collecting this article
     */
    val collectUserId: Long = Long.Default,

    /**
     * ID of article
     */
    val articleId: Long = Long.Default,

    /**
     * Collect time
     */
    val timestamp: Long = Long.Default
)
