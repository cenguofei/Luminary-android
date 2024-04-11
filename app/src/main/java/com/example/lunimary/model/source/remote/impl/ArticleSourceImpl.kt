package com.example.lunimary.model.source.remote.impl

import com.example.lunimary.base.ktor.addPagesParam
import com.example.lunimary.base.ktor.addPathParam
import com.example.lunimary.base.ktor.init
import com.example.lunimary.base.ktor.securityDelete
import com.example.lunimary.base.ktor.securityGet
import com.example.lunimary.base.ktor.securityPost
import com.example.lunimary.base.ktor.securityPut
import com.example.lunimary.base.ktor.setJsonBody
import com.example.lunimary.model.Article
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.responses.PageResponse
import com.example.lunimary.model.source.remote.ArticleSource
import com.example.lunimary.util.articlesRootPath
import com.example.lunimary.util.createArticlePath
import com.example.lunimary.util.isArticleDeletedPath
import com.example.lunimary.util.pageArticlesPath
import com.example.lunimary.util.updateArticleByIdPath
import com.example.lunimary.util.whenBrowseArticlePath
import io.ktor.client.request.get

class ArticleSourceImpl: BaseSourceImpl by BaseSourceImpl(), ArticleSource {
    override suspend fun getArticleById(id: Long): DataResponse<Article> {
        return client.get(urlString = articlesRootPath) {
            addPathParam(id)
        }.init()
    }

    override suspend fun deleteArticleById(id: Long): DataResponse<Unit> {
        return client.securityDelete(urlString = articlesRootPath) {
            addPathParam(id)
        }.init()
    }

    override suspend fun updateArticle(article: Article): DataResponse<Unit> {
        return client.securityPut(urlString = updateArticleByIdPath) {
            setJsonBody(article)
        }.init()
    }

    override suspend fun addArticle(article: Article): DataResponse<Unit> {
        return client.securityPost(urlString = createArticlePath) {
            setJsonBody(article)
        }.init()
    }

    override suspend fun allArticles(curPage: Int, perPageCount: Int): PageResponse<Article> {
        return client.get(urlString = pageArticlesPath) {
            addPagesParam(curPage, perPageCount)
        }.init()
    }

    override suspend fun whenBrowseArticle(articleId: Long): DataResponse<Boolean> {
        return client.securityGet(urlString = whenBrowseArticlePath) {
            addPathParam(articleId)
        }.init()
    }

    override suspend fun existing(articleId: Long): DataResponse<Boolean> {
        return client.get(isArticleDeletedPath) {
            addPathParam(articleId)
        }.init()
    }
}