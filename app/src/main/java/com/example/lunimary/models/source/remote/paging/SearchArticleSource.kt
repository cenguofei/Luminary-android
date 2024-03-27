package com.example.lunimary.models.source.remote.paging

import com.example.lunimary.models.Article
import com.example.lunimary.models.ktor.addPagesParam
import com.example.lunimary.models.ktor.addQueryParam
import com.example.lunimary.models.ktor.init
import com.example.lunimary.models.responses.PageResponse
import com.example.lunimary.models.source.remote.PageSource
import com.example.lunimary.models.source.remote.impl.BaseSourceImpl
import com.example.lunimary.util.empty
import com.example.lunimary.util.searchArticlePath
import io.ktor.client.request.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchArticleSource(
    private val searchContent: String
) : PageSource<Article>, BaseSourceImpl by BaseSourceImpl() {

    override suspend fun pages(
        curPage: Int,
        perPageCount: Int
    ): PageResponse<Article> {
        return client.get(urlString = searchArticlePath) {
            addPagesParam(curPage, perPageCount)
            addQueryParam("searchContent",searchContent)
        }.init()
    }
}