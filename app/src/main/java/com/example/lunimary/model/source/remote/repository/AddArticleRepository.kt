package com.example.lunimary.model.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.model.Article
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.AddArticleSource

class AddArticleRepository : Repository by Repository() {
    private val addArticleSource = AddArticleSource

    suspend fun addArticle(article: Article): DataResponse<Unit> =
        withDispatcher { addArticleSource.addArticle(article) }
}