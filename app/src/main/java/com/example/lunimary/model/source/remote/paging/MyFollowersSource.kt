package com.example.lunimary.model.source.remote.paging

import com.example.lunimary.base.currentUser
import com.example.lunimary.base.ktor.addPagesParam
import com.example.lunimary.base.ktor.addPathParam
import com.example.lunimary.base.ktor.init
import com.example.lunimary.model.ext.FollowersInfo
import com.example.lunimary.model.responses.PageResponse
import com.example.lunimary.model.source.remote.impl.BaseSourceImpl
import com.example.lunimary.util.pageMyFollowersPath
import io.ktor.client.request.get

class MyFollowersSource : PageSource<FollowersInfo>, BaseSourceImpl by BaseSourceImpl() {
    override suspend fun pages(curPage: Int, perPageCount: Int): PageResponse<FollowersInfo> {
        return client.get(urlString = pageMyFollowersPath) {
            addPathParam(currentUser.id)
            addPagesParam(curPage, perPageCount)
        }.init()
    }

    companion object : PageSource<FollowersInfo> by MyFollowersSource()
}