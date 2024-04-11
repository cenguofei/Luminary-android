package com.example.lunimary.model.source.remote.paging

import com.example.lunimary.model.ext.UserFriend
import com.example.lunimary.model.responses.PageResponse
import com.example.lunimary.model.source.remote.MessageSource
import com.example.lunimary.model.source.remote.impl.BaseSourceImpl

class MessageFollowSource : PageSource<UserFriend>, BaseSourceImpl by BaseSourceImpl() {
    override suspend fun pages(curPage: Int, perPageCount: Int): PageResponse<UserFriend> {
        return MessageSource.messageForFollows(curPage, perPageCount)
    }

    companion object : PageSource<UserFriend> by MessageFollowSource()
}