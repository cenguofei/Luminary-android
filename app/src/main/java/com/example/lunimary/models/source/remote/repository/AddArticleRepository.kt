package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.BaseRepository
import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.AddArticleSource
import com.example.lunimary.models.source.remote.impl.AddArticleSourceImpl

class AddArticleRepository : BaseRepository by BaseRepository(), AddArticleSource {
    private val addArticleSource: AddArticleSource = AddArticleSourceImpl()

    override suspend fun addArticle(article: Article): DataResponse<Unit> =
        withDispatcher { addArticleSource.addArticle(article) }
}