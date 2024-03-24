package com.example.lunimary.models.source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.DEFAULT_PER_PAGE_COUNT
import com.example.lunimary.models.source.remote.PageSource
import com.example.lunimary.models.source.remote.repository.ArticleRepository

class ArticlePagingSource(
    private val source: PageSource<Article>
) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val wishPage = params.key ?: 0
            val pageResponse = source.pages(wishPage, DEFAULT_PER_PAGE_COUNT)
            if (pageResponse.isSuccess()) {
                val data = pageResponse.data
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

