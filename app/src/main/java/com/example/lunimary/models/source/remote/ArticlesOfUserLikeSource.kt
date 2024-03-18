package com.example.lunimary.models.source.remote

import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.DataResponse

interface ArticlesOfUserLikeSource {
    /**
     * 用户点赞的文章
     */
    suspend fun likesOfUser(
        userId: Long
    ): DataResponse<List<Article>>
}