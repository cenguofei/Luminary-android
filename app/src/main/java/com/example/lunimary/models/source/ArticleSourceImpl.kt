package com.example.lunimary.models.source

import com.example.lunimary.models.Article
import com.example.lunimary.models.ktor.addJson
import com.example.lunimary.models.ktor.addPagesParam
import com.example.lunimary.models.ktor.httpClient
import com.example.lunimary.models.ktor.securityDelete
import com.example.lunimary.models.ktor.securityPost
import com.example.lunimary.models.ktor.securityPut
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.PageResponse
import com.example.lunimary.util.articlesRootPath
import com.example.lunimary.util.createArticlePath
import com.example.lunimary.util.deleteArticleByIdPath
import com.example.lunimary.util.getArticleByIdPath
import com.example.lunimary.util.pageArticlesPath
import com.example.lunimary.util.updateArticleByIdPath
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import io.ktor.http.path

class ArticleSourceImpl(private val client: HttpClient = httpClient) : ArticleSource {
    override suspend fun getArticleById(id: Long): DataResponse<Article> {
        return client.get(urlString = articlesRootPath) {
            url {
                appendPathSegments(id.toString())
            }
        }.let { it.body<DataResponse<Article>>().init(it) }
    }

    override suspend fun deleteArticleById(id: Long): DataResponse<Unit> {
        return client.securityDelete(urlString = articlesRootPath) {
            url {
                appendPathSegments(id.toString())
            }
        }.let { it.body<DataResponse<Unit>>().init(it) }
    }

    override suspend fun updateArticle(article: Article): DataResponse<Unit> {
        return client.securityPut(urlString = updateArticleByIdPath) {
            addJson(article)
        }.let { it.body<DataResponse<Unit>>().init(it) }
    }

    override suspend fun addArticle(article: Article): DataResponse<Unit> {
        return client.securityPost(urlString = createArticlePath) {
            addJson(article)
        }.let { it.body<DataResponse<Unit>>().init(it) }
    }

    override suspend fun allArticles(curPage: Int, perPageCount: Int): PageResponse<Article> {
        return client.get(urlString = pageArticlesPath) {
            addPagesParam(curPage, perPageCount)
        }.let { it.body<PageResponse<Article>>().init(it) }
    }

    override suspend fun recommendedArticles(
        curPage: Int,
        perPageCount: Int
    ): PageResponse<Article> {
        return client.get(urlString = pageArticlesPath) {
            addPagesParam(curPage, perPageCount)
        }.let { it.body<PageResponse<Article>>().init(it) }
    }
}