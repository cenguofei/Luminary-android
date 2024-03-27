package com.example.lunimary.models.source.remote

import com.example.lunimary.models.Article
import com.example.lunimary.models.User
import com.example.lunimary.models.responses.PageResponse

interface SearchSource {
    suspend fun searchArticle(
        searchContent: String,
        curPage: Int,
        perPageCount: Int
    ): PageResponse<Article>

    suspend fun searchUser(
        searchContent: String,
        curPage: Int,
        perPageCount: Int
    ): PageResponse<User>
}