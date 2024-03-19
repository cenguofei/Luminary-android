package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.models.Article
import com.example.lunimary.models.ktor.setJsonBody
import com.example.lunimary.models.ktor.addPagesParam
import com.example.lunimary.models.ktor.init
import com.example.lunimary.models.ktor.securityDelete
import com.example.lunimary.models.ktor.securityPost
import com.example.lunimary.models.ktor.securityPut
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.responses.PageResponse
import com.example.lunimary.models.source.remote.ArticleSource
import com.example.lunimary.util.articlesRootPath
import com.example.lunimary.util.createArticlePath
import com.example.lunimary.util.currentUser
import com.example.lunimary.util.pageArticlesPath
import com.example.lunimary.util.privacyArticlesOfUserPath
import com.example.lunimary.util.publicArticlesOfUserPath
import com.example.lunimary.util.updateArticleByIdPath
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments

class ArticleSourceImpl: BaseSourceImpl by BaseSourceImpl(), ArticleSource {
    override suspend fun getArticleById(id: Long): DataResponse<Article> {
        return client.get(urlString = articlesRootPath) {
            url {
                appendPathSegments(id.toString())
            }
        }.init()
    }

    override suspend fun deleteArticleById(id: Long): DataResponse<Unit> {
        return client.securityDelete(urlString = articlesRootPath) {
            url {
                appendPathSegments(id.toString())
            }
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

    override suspend fun recommendedArticles(
        curPage: Int,
        perPageCount: Int
    ): PageResponse<Article> {
        return client.get(urlString = pageArticlesPath) {
            addPagesParam(curPage, perPageCount)
        }.init()
    }

    override suspend fun publicArticles(userId: Long): DataResponse<List<Article>> {
        return client.get(urlString = publicArticlesOfUserPath) {
            url {
                appendPathSegments(currentUser.id.toString())
            }
        }.init()
    }

    override suspend fun privacyArticles(userId: Long): DataResponse<List<Article>> {
        return client.get(urlString = privacyArticlesOfUserPath) {
            url {
                appendPathSegments(currentUser.id.toString())
            }
        }.init()
    }
}