package com.example.lunimary.models.source.remote

import com.example.lunimary.models.Article
import com.example.lunimary.models.ktor.addUserIdPath
import com.example.lunimary.models.ktor.httpClient
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.util.getAllArticlesOfUserCollectedPath
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class CollectSourceImpl(
    private val client: HttpClient = httpClient
) : CollectSource {
    override suspend fun collectsOfUser(userId: Long): DataResponse<List<Article>> {
        return client.get(getAllArticlesOfUserCollectedPath) {
            addUserIdPath(userId)
        }.let { it.body<DataResponse<List<Article>>>().init(it) }
    }
}