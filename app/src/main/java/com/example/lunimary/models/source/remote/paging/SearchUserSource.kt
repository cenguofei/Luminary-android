package com.example.lunimary.models.source.remote.paging

import com.example.lunimary.models.Article
import com.example.lunimary.models.User
import com.example.lunimary.models.ktor.addPagesParam
import com.example.lunimary.models.ktor.addQueryParam
import com.example.lunimary.models.ktor.init
import com.example.lunimary.models.responses.PageResponse
import com.example.lunimary.models.source.remote.PageSource
import com.example.lunimary.models.source.remote.impl.BaseSourceImpl
import com.example.lunimary.util.searchArticlePath
import com.example.lunimary.util.searchUserPath
import io.ktor.client.request.get

class SearchUserSource(
    private val searchContent: String
) : PageSource<User>, BaseSourceImpl by BaseSourceImpl() {
    override suspend fun pages(
        curPage: Int,
        perPageCount: Int
    ): PageResponse<User> {
        return client.get(urlString = searchUserPath) {
            addPagesParam(curPage, perPageCount)
            addQueryParam("searchContent",searchContent)
        }.init()
    }
}