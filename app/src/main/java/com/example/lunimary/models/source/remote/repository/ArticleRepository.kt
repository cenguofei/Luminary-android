package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.BaseRepository
import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.PageResponse
import com.example.lunimary.models.source.remote.ArticleSource
import com.example.lunimary.models.source.remote.impl.ArticleSourceImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticleRepository : BaseRepository by BaseRepository(), ArticleSource {
    private val articleSource: ArticleSource = ArticleSourceImpl()

    override suspend fun getArticleById(id: Long): DataResponse<Article> =
        withDispatcher { articleSource.getArticleById(id) }

    override suspend fun deleteArticleById(id: Long): DataResponse<Unit> =
        withDispatcher { articleSource.deleteArticleById(id) }

    override suspend fun updateArticle(article: Article): DataResponse<Unit> =
        withDispatcher { articleSource.updateArticle(article) }

    override suspend fun addArticle(article: Article): DataResponse<Unit> =
        withDispatcher { articleSource.addArticle(article) }

    override suspend fun allArticles(curPage: Int, perPageCount: Int): PageResponse<Article> =
        withDispatcher { articleSource.allArticles(curPage, perPageCount) }

    override suspend fun recommendedArticles(
        curPage: Int,
        perPageCount: Int
    ): PageResponse<Article> =
        withDispatcher { articleSource.recommendedArticles(curPage, perPageCount) }

    override suspend fun publicArticles(userId: Long): DataResponse<List<Article>> =
        withDispatcher { articleSource.publicArticles(userId) }

    override suspend fun privacyArticles(userId: Long): DataResponse<List<Article>> =
        withDispatcher { articleSource.privacyArticles(userId) }
}