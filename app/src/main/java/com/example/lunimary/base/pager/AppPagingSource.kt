package com.example.lunimary.base.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lunimary.models.responses.DEFAULT_PER_PAGE_COUNT
import com.example.lunimary.models.source.remote.paging.PageSource

class AppPagingSource<T : Any>(
    private val source: PageSource<T>
) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val wishPage = params.key ?: 0
            val pageResponse = source.pages(wishPage, DEFAULT_PER_PAGE_COUNT)
            if (pageResponse.isSuccess()) {
                val data = pageResponse.data
//                LoadResult.Page(
//                    data = emptyList(),
//                    prevKey = null,
//                    nextKey = null
//                )
//                LoadResult.Error(Throwable(message = pageResponse.msg))
                LoadResult.Page(
                    data = data?.lists ?: emptyList(),
                    prevKey = if (wishPage == 0) null else wishPage - 1,
                    nextKey = if (wishPage < data?.pageSize!!) wishPage + 1 else null
                )
            } else {
                LoadResult.Error(Throwable(message = pageResponse.msg))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

