package com.example.lunimary.model.source.remote.paging

import com.example.lunimary.model.ext.CommentItem
import com.example.lunimary.model.responses.PageResponse
import com.example.lunimary.model.source.remote.MessageSource
import com.example.lunimary.model.source.remote.impl.BaseSourceImpl

class MessageCommentSource : PageSource<CommentItem>, BaseSourceImpl by BaseSourceImpl() {
    override suspend fun pages(curPage: Int, perPageCount: Int): PageResponse<CommentItem> {
        return MessageSource.messageForComments(curPage, perPageCount)
    }

    companion object : PageSource<CommentItem> by MessageCommentSource()
}