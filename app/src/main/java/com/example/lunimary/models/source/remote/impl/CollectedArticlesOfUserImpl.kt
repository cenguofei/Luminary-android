package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.models.Article
import com.example.lunimary.base.ktor.addUserIdPath
import com.example.lunimary.base.ktor.init
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.CollectedArticlesOfUser
import com.example.lunimary.util.getAllArticlesOfUserCollectedPath
import io.ktor.client.request.get

class CollectedArticlesOfUserImpl: BaseSourceImpl by BaseSourceImpl(),  CollectedArticlesOfUser {
    override suspend fun collectsOfUser(userId: Long): DataResponse<List<Article>> {
        return client.get(getAllArticlesOfUserCollectedPath) {
            addUserIdPath(userId)
        }.init()
    }
}