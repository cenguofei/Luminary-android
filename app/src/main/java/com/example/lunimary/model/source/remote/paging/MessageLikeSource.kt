package com.example.lunimary.model.source.remote.paging

import com.example.lunimary.model.LikeMessage
import com.example.lunimary.model.responses.PageResponse
import com.example.lunimary.model.source.remote.MessageSource
import com.example.lunimary.model.source.remote.impl.BaseSourceImpl

class MessageLikeSource : PageSource<LikeMessage>, BaseSourceImpl by BaseSourceImpl() {
    override suspend fun pages(curPage: Int, perPageCount: Int): PageResponse<LikeMessage> {
        return MessageSource.messageForLikes(curPage, perPageCount)
    }

    companion object : PageSource<LikeMessage> by MessageLikeSource()
}