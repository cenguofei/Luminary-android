package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.models.Article
import com.example.lunimary.models.ktor.init
import com.example.lunimary.models.ktor.setJsonBody
import com.example.lunimary.models.ktor.securityPost
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.AddArticleSource
import com.example.lunimary.util.createArticlePath
import io.ktor.client.call.body

class AddArticleSourceImpl: BaseSourceImpl by BaseSourceImpl(), AddArticleSource {
    override suspend fun addArticle(article: Article): DataResponse<Unit> {
        return client.securityPost(urlString = createArticlePath){
            setJsonBody(article)
        }.init()
    }
}