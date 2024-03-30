package com.example.lunimary.models.ext

import com.example.lunimary.models.Article
import com.example.lunimary.models.Comment
import com.example.lunimary.models.User

@kotlinx.serialization.Serializable
data class CommentItem(
    val user: User,
    val article: Article,
    val comment: Comment
)

typealias CommentItems = List<CommentItem>