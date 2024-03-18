package com.example.lunimary.models

import com.example.lunimary.util.Default
import java.text.SimpleDateFormat

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

fun main() {
    val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd")


    val niceDate: String = dateTimeFormat.format(System.currentTimeMillis())
    println("niceDate=$niceDate")
}
