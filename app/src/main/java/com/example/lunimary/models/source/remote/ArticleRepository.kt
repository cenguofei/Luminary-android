package com.example.lunimary.models.source.remote

import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.PageResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticleRepository(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ArticleSource {
    private val articleSource: ArticleSource = ArticleSourceImpl()

    override suspend fun getArticleById(id: Long): DataResponse<Article> =
        withContext(dispatcher) { articleSource.getArticleById(id) }

    override suspend fun deleteArticleById(id: Long): DataResponse<Unit> =
        withContext(dispatcher) { articleSource.deleteArticleById(id) }

    override suspend fun updateArticle(article: Article): DataResponse<Unit> =
        withContext(dispatcher) { articleSource.updateArticle(article) }

    override suspend fun addArticle(article: Article): DataResponse<Unit> =
        withContext(dispatcher) { articleSource.addArticle(article) }

    override suspend fun allArticles(curPage: Int, perPageCount: Int): PageResponse<Article> =
        withContext(dispatcher) { articleSource.allArticles(curPage, perPageCount) }

    override suspend fun recommendedArticles(
        curPage: Int,
        perPageCount: Int
    ): PageResponse<Article> =
        withContext(dispatcher) { articleSource.recommendedArticles(curPage, perPageCount) }

    override suspend fun publicArticles(userId: Long): DataResponse<List<Article>> =
        withContext(dispatcher) { articleSource.publicArticles(userId) }

    override suspend fun privacyArticles(userId: Long): DataResponse<List<Article>> =
        withContext(dispatcher) { articleSource.privacyArticles(userId) }
}