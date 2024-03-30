package com.example.lunimary.models.ext

import com.example.lunimary.models.Comment
import com.example.lunimary.models.User

@kotlinx.serialization.Serializable
data class CommentsWithUser(
    val user: User? = null,
    val comments: List<Comment> = emptyList()
)
