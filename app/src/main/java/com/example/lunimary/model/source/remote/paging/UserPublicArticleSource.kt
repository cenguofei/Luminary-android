package com.example.lunimary.model.source.remote.paging

import com.example.lunimary.base.ktor.addPagesParam
import com.example.lunimary.base.ktor.addPathParam
import com.example.lunimary.base.ktor.init
import com.example.lunimary.model.Article
import com.example.lunimary.model.responses.PageResponse
import com.example.lunimary.model.source.remote.impl.BaseSourceImpl
import com.example.lunimary.util.publicArticlesOfUserPath
import io.ktor.client.request.get

class UserPublicArticleSource(
    private val userId: Long
) : PageSource<Article>, BaseSourceImpl by BaseSourceImpl() {
    override suspend fun pages(curPage: Int, perPageCount: Int): PageResponse<Article> {
        return client.get(urlString = publicArticlesOfUserPath) {
            addPagesParam(curPage, perPageCount)
            addPathParam(userId)
        }.init()
    }
}