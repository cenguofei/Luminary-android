package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.models.Article
import com.example.lunimary.base.ktor.addUserIdPath
import com.example.lunimary.base.ktor.init
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.ArticlesOfUserLikeSource
import com.example.lunimary.util.getAllArticlesOfUserLikedPath
import io.ktor.client.request.get

class ArticlesOfUserLikeSourceImpl: BaseSourceImpl by BaseSourceImpl(),  ArticlesOfUserLikeSource {
    override suspend fun likesOfUser(userId: Long): DataResponse<List<Article>> {
        return client.get(getAllArticlesOfUserLikedPath) {
            addUserIdPath(userId)
        }.init()
    }
}