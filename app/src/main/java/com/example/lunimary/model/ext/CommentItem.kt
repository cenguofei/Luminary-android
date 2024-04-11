package com.example.lunimary.model.ext

import com.example.lunimary.model.Article
import com.example.lunimary.model.Comment
import com.example.lunimary.model.User

@kotlinx.serialization.Serializable
data class CommentItem(
    val user: User,
    val article: Article,
    val comment: Comment
)