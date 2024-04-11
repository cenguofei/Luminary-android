package com.example.lunimary.model.ext

import com.example.lunimary.model.Comment
import com.example.lunimary.model.User

@kotlinx.serialization.Serializable
data class CommentsWithUser(
    val user: User? = null,
    val comments: List<Comment> = emptyList()
)
