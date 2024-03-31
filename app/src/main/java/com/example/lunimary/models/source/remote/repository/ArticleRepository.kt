package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.PageResponse
import com.example.lunimary.models.source.remote.ArticleSource

class ArticleRepository : Repository by Repository() {
    private val articleSource = ArticleSource

    suspend fun getArticleById(id: Long): DataResponse<Article> =
        withDispatcher { articleSource.getArticleById(id) }

    suspend fun deleteArticleById(id: Long): DataResponse<Unit> =
        withDispatcher { articleSource.deleteArticleById(id) }

    suspend fun updateArticle(article: Article): DataResponse<Unit> =
        withDispatcher { articleSource.updateArticle(article) }

    suspend fun addArticle(article: Article): DataResponse<Unit> =
        withDispatcher { articleSource.addArticle(article) }

    suspend fun allArticles(curPage: Int, perPageCount: Int): PageResponse<Article> =
        withDispatcher { articleSource.allArticles(curPage, perPageCount) }

    suspend fun whenBrowseArticle(articleId: Long): DataResponse<Boolean> =
        withDispatcher { articleSource.whenBrowseArticle(articleId) }
}