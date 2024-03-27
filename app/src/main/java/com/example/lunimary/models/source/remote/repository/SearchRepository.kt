package com.example.lunimary.models.source.remote.repository

import com.example.lunimary.base.BaseRepository
import com.example.lunimary.models.Article
import com.example.lunimary.models.User
import com.example.lunimary.models.responses.PageResponse
import com.example.lunimary.models.source.remote.SearchSource
import com.example.lunimary.models.source.remote.impl.SearchSourceImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class SearchRepository : BaseRepository by BaseRepository() {
    private val source: SearchSource = SearchSourceImpl()

    suspend fun searchArticle(
        searchContent: String,
        curPage: Int,
        perPageCount: Int
    ): Flow<PageResponse<Article>> = flow {
        val result = withDispatcher {
            source.searchArticle(searchContent, curPage, perPageCount)
        }
        emit(result)
    }

    suspend fun searchUser(
        searchContent: String,
        curPage: Int,
        perPageCount: Int
    ): Flow<PageResponse<User>> = flow {
        val result = withDispatcher {
            source.searchUser(searchContent, curPage, perPageCount)
        }
        emit(result)
    }
}