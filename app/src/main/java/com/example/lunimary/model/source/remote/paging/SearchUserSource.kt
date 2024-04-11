package com.example.lunimary.model.source.remote.paging

import com.example.lunimary.base.ktor.addPagesParam
import com.example.lunimary.base.ktor.addQueryParam
import com.example.lunimary.base.ktor.init
import com.example.lunimary.model.User
import com.example.lunimary.model.responses.PageResponse
import com.example.lunimary.model.source.remote.impl.BaseSourceImpl
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