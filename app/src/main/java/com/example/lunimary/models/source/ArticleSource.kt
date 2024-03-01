package com.example.lunimary.models.source

import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.PageResponse

interface ArticleSource {
    suspend fun getArticleById(id: Long) : DataResponse<Article>

    suspend fun deleteArticleById(id: Long) : DataResponse<Unit>

    suspend fun updateArticle(article: Article) : DataResponse<Unit>

    suspend fun addArticle(article: Article) : DataResponse<Unit>

    suspend fun allArticles(curPage: Int, perPageCount: Int) : PageResponse<Article>

    suspend fun recommendedArticles(curPage: Int, perPageCount: Int) : PageResponse<Article>
}