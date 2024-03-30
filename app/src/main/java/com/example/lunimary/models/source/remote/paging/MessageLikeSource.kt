package com.example.lunimary.models.source.remote.paging

import com.example.lunimary.models.LikeMessage
import com.example.lunimary.models.responses.PageResponse
import com.example.lunimary.models.source.remote.MessageSource
import com.example.lunimary.models.source.remote.PageSource
import com.example.lunimary.models.source.remote.impl.BaseSourceImpl

class MessageLikeSource : PageSource<LikeMessage>, BaseSourceImpl by BaseSourceImpl() {
    override suspend fun pages(curPage: Int, perPageCount: Int): PageResponse<LikeMessage> {
        return MessageSource.messageForLikes(curPage, perPageCount)
    }

    companion object : PageSource<LikeMessage> by MessageLikeSource()
}