package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.AddArticleSource

class AddArticleRepository : Repository by Repository() {
    private val addArticleSource = AddArticleSource

    suspend fun addArticle(article: Article): DataResponse<Unit> =
        withDispatcher { addArticleSource.addArticle(article) }
}