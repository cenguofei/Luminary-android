package com.example.lunimary.models

import com.example.lunimary.base.niceDateToSecond
import com.example.lunimary.models.responses.DataResponse

@kotlinx.serialization.Serializable
data class LikeMessage(
    val likeUser: User, //点赞人
    val article: Article, //点赞的文章
    val timestamp: Long
) {
    val niceDate: String get() = timestamp.niceDateToSecond
}

typealias LikeMessageResponse = DataResponse<List<LikeMessage>>