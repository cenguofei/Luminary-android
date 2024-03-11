package com.example.lunimary.models.source.remote

import com.example.lunimary.models.Article
import com.example.lunimary.models.ktor.addJson
import com.example.lunimary.models.ktor.httpClient
import com.example.lunimary.models.ktor.securityPost
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.util.createArticlePath
import io.ktor.client.HttpClient
import io.ktor.client.call.body

class AddArticleSourceImpl(
    private val client: HttpClient = httpClient
) : AddArticleSource {

    override suspend fun addArticle(article: Article): DataResponse<Unit> {
        return client.securityPost(urlString = createArticlePath){
            addJson(article)
        }.let { it.body<DataResponse<Unit>>().init(it) }
    }
}