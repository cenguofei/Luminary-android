package com.example.lunimary.model

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
     * Should the like visible to article owner.
     */
    val visibleToOwner: Boolean = true,

    /**
     * Like time
     */
    val timestamp: Long = Long.Default
)