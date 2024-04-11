package com.example.lunimary.model.source.remote.paging

import com.example.lunimary.model.responses.PageResponse

interface PageSource<T> {
    suspend fun pages(
        curPage: Int,
        perPageCount: Int
    ): PageResponse<T>
}