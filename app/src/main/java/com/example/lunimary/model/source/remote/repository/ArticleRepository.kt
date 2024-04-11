package com.example.lunimary.model.source.remote.repository

import com.example.lunimary.base.Repository
import com.example.lunimary.model.Article
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.responses.PageResponse
import com.example.lunimary.model.source.remote.ArticleSource

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

    suspend fun existing(articleId: Long): DataResponse<Boolean> =
        withDispatcher { articleSource.existing(articleId) }
}