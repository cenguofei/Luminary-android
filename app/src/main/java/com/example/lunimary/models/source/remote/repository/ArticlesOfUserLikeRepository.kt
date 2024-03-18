package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.BaseRepository
import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.ArticlesOfUserLikeSource
import com.example.lunimary.models.source.remote.impl.ArticlesOfUserLikeSourceImpl

class ArticlesOfUserLikeRepository : BaseRepository by BaseRepository(), ArticlesOfUserLikeSource {
    private val likeSource: ArticlesOfUserLikeSource = ArticlesOfUserLikeSourceImpl()

    override suspend fun likesOfUser(userId: Long): DataResponse<List<Article>> =
        withDispatcher { likeSource.likesOfUser(userId) }
}