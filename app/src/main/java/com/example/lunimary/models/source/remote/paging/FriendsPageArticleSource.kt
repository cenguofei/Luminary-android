package com.example.lunimary.models.source.remote.paging

import com.example.lunimary.models.Article
import com.example.lunimary.base.ktor.addPagesParam
import com.example.lunimary.base.ktor.addQueryParam
import com.example.lunimary.base.ktor.init
import com.example.lunimary.models.responses.PageResponse
import com.example.lunimary.models.source.remote.PageSource
import com.example.lunimary.base.currentUser
import com.example.lunimary.util.pageFriendsArticlesPath
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FriendsPageArticleSource : PageSource<Article>, BasePageSource by BasePageSource() {
    override suspend fun pages(curPage: Int, perPageCount: Int): PageResponse<Article> {
        return withContext(Dispatchers.IO) {
            client.get(pageFriendsArticlesPath) {
                addPagesParam(curPage, perPageCount)
                addQueryParam("loginUserId", currentUser.id)
            }.init()
        }
    }
}