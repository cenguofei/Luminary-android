package com.example.lunimary.models.source.remote.paging

import com.example.lunimary.models.ext.UserFriend
import com.example.lunimary.models.responses.PageResponse
import com.example.lunimary.models.source.remote.MessageSource
import com.example.lunimary.models.source.remote.PageSource
import com.example.lunimary.models.source.remote.impl.BaseSourceImpl

class MessageFollowSource : PageSource<UserFriend>, BaseSourceImpl by BaseSourceImpl() {
    override suspend fun pages(curPage: Int, perPageCount: Int): PageResponse<UserFriend> {
        return MessageSource.messageForFollows(curPage, perPageCount)
    }

    companion object : PageSource<UserFriend> by MessageFollowSource()
}