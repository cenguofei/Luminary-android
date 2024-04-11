package com.example.lunimary.model.source.remote.paging

import com.example.lunimary.base.ktor.addPagesParam
import com.example.lunimary.base.ktor.init
import com.example.lunimary.model.Article
import com.example.lunimary.model.responses.PageResponse
import com.example.lunimary.util.pageArticlesPath
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PageAllArticleSource : PageSource<Article>, BasePageSource by BasePageSource() {
    override suspend fun pages(curPage: Int, perPageCount: Int): PageResponse<Article> {
        return withContext(Dispatchers.IO) {
            client.get(urlString = pageArticlesPath) {
                addPagesParam(curPage, perPageCount)
            }.init()
        }
    }

    companion object : PageSource<Article> by PageAllArticleSource()
}