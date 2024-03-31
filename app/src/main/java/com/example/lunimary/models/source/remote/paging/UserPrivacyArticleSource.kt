package com.example.lunimary.models.source.remote.paging

import com.example.lunimary.base.currentUser
import com.example.lunimary.base.ktor.addPagesParam
import com.example.lunimary.base.ktor.addPathParam
import com.example.lunimary.base.ktor.init
import com.example.lunimary.base.ktor.securityGet
import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.PageResponse
import com.example.lunimary.models.source.remote.impl.BaseSourceImpl
import com.example.lunimary.util.privacyArticlesOfUserPath

class UserPrivacyArticleSource : PageSource<Article>, BaseSourceImpl by BaseSourceImpl() {
    override suspend fun pages(curPage: Int, perPageCount: Int): PageResponse<Article> {
        return client.securityGet(urlString = privacyArticlesOfUserPath) {
            addPagesParam(curPage, perPageCount)
            addPathParam(currentUser.id)
        }.init()
    }

    companion object : PageSource<Article> by UserPrivacyArticleSource()
}