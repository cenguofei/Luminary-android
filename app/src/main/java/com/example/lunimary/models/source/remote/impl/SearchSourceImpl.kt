package com.example.lunimary.models.source.remote.impl

import com.example.lunimary.models.Article
import com.example.lunimary.models.User
import com.example.lunimary.base.ktor.addPagesParam
import com.example.lunimary.base.ktor.addQueryParam
import com.example.lunimary.base.ktor.init
import com.example.lunimary.models.responses.PageResponse
import com.example.lunimary.models.source.remote.SearchSource
import com.example.lunimary.util.searchArticlePath
import com.example.lunimary.util.searchUserPath
import io.ktor.client.request.get

class SearchSourceImpl : BaseSourceImpl by BaseSourceImpl(), SearchSource {

    override suspend fun searchArticle(
        searchContent: String,
        curPage: Int,
        perPageCount: Int
    ): PageResponse<Article> {
        return client.get(urlString = searchArticlePath) {
            addPagesParam(curPage, perPageCount)
            addQueryParam("searchContent",searchContent)
        }.init()
    }

    override suspend fun searchUser(
        searchContent: String,
        curPage: Int,
        perPageCount: Int
    ): PageResponse<User> {
        return client.get(urlString = searchUserPath) {
            addPagesParam(curPage, perPageCount)
            addQueryParam("searchContent",searchContent)
        }.init()
    }
}