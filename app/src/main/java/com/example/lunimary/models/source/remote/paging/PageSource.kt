package com.example.lunimary.models.source.remote.paging

import com.example.lunimary.models.responses.PageResponse

interface PageSource<T> {
    suspend fun pages(
        curPage: Int,
        perPageCount: Int
    ): PageResponse<T>
}