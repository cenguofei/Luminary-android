package com.example.lunimary.base.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lunimary.model.responses.DEFAULT_PER_PAGE_COUNT
import com.example.lunimary.model.source.remote.paging.PageSource

/**
 * 分页请求后台数据
 * @param source 分页数据来源
 * @see  [PageSource]
 * @see  [PageItem]
 */
class AppPagingSource<T : Any>(
    private val source: PageSource<T>
) : PagingSource<Int, PageItem<T>>() {
    override fun getRefreshKey(state: PagingState<Int, PageItem<T>>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PageItem<T>> {
        return try {
            val wishPage = params.key ?: 0
            val pageResponse = source.pages(wishPage, DEFAULT_PER_PAGE_COUNT)
            if (pageResponse.isSuccess()) {
                val data = pageResponse.data
                LoadResult.Page(
                    data = data?.lists?.map {
                        PageItem(it)
                    } ?: emptyList(),
                    prevKey = if (wishPage == 0) null else wishPage - 1,
                    nextKey = if (wishPage < (data?.pageSize!! - 1)) wishPage + 1 else null
                )
            } else {
                LoadResult.Error(Throwable(message = pageResponse.msg))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

