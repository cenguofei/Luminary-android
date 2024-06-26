package com.example.lunimary.model

import com.example.lunimary.base.niceDateToDay
import com.example.lunimary.util.Default
import com.example.lunimary.util.empty

@kotlinx.serialization.Serializable
data class Comment(
    val id: Long = Long.Default,

    /**
     * The user ID who commented on this article.
     */
    val userId: Long = Long.Default,

    /**
     * Comment content
     */
    val content: String = empty,

    /**
     * ID of article
     */
    val articleId: Long = Long.Default,

    /**
     * Comment time
     */
    val timestamp: Long = Long.Default
) {
    val niceDate: String get() = timestamp.niceDateToDay
}
