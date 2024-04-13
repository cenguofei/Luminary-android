package com.example.lunimary.model

import com.example.lunimary.base.niceDateToSecond

@kotlinx.serialization.Serializable
data class LikeMessage(
    val likeId: Long,
    val likeUser: User, //点赞人
    val article: Article, //点赞的文章
    val timestamp: Long,
) {
    val niceDate: String get() = timestamp.niceDateToSecond
}