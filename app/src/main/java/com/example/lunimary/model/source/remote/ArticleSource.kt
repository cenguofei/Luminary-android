package com.example.lunimary.model.source.remote

import com.example.lunimary.model.Article
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.responses.PageResponse
import com.example.lunimary.model.source.remote.impl.ArticleSourceImpl

interface ArticleSource {
    suspend fun getArticleById(id: Long) : DataResponse<Article>

    suspend fun deleteArticleById(id: Long) : DataResponse<Unit>

    suspend fun updateArticle(article: Article) : DataResponse<Unit>

    suspend fun addArticle(article: Article) : DataResponse<Unit>

    suspend fun allArticles(curPage: Int, perPageCount: Int) : PageResponse<Article>

    suspend fun whenBrowseArticle(
        articleId: Long
    ): DataResponse<Boolean>

    suspend fun existing(
        articleId: Long
    ): DataResponse<Boolean>

    companion object : ArticleSource by ArticleSourceImpl()
}