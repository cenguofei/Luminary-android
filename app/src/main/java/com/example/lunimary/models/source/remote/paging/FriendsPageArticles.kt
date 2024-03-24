package com.example.lunimary.models.source.remote.paging

import com.example.lunimary.models.Article
import com.example.lunimary.models.ktor.addPagesParam
import com.example.lunimary.models.ktor.addQueryParam
import com.example.lunimary.models.ktor.init
import com.example.lunimary.models.responses.PageResponse
import com.example.lunimary.models.source.remote.PageSource
import com.example.lunimary.util.currentUser
import com.example.lunimary.util.pageFriendsArticlesPath
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FriendsPageArticles : PageSource<Article>, BasePageSource by BasePageSource() {
    override suspend fun pages(curPage: Int, perPageCount: Int): PageResponse<Article> {
        return withContext(Dispatchers.IO) {
            client.get(pageFriendsArticlesPath) {
                addPagesParam(curPage, perPageCount)
                addQueryParam("loginUserId", currentUser.id)
            }.init()
        }
    }
}