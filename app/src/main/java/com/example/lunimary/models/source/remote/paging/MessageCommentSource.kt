package com.example.lunimary.models.source.remote.paging

import com.example.lunimary.models.ext.CommentItem
import com.example.lunimary.models.responses.PageResponse
import com.example.lunimary.models.source.remote.MessageSource
import com.example.lunimary.models.source.remote.PageSource
import com.example.lunimary.models.source.remote.impl.BaseSourceImpl

class MessageCommentSource : PageSource<CommentItem>, BaseSourceImpl by BaseSourceImpl() {
    override suspend fun pages(curPage: Int, perPageCount: Int): PageResponse<CommentItem> {
        return MessageSource.messageForComments(curPage, perPageCount)
    }

    companion object : PageSource<CommentItem> by MessageCommentSource()
}