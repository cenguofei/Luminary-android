package com.example.lunimary.models.source.remote

import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.DataResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddArticleRepository(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AddArticleSource {
    private val addArticleSource: AddArticleSource = AddArticleSourceImpl()

    override suspend fun addArticle(article: Article): DataResponse<Unit> =
        withContext(dispatcher) { addArticleSource.addArticle(article) }
}