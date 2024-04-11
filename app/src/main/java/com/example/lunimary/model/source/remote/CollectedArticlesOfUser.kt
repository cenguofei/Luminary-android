package com.example.lunimary.model.source.remote

import com.example.lunimary.model.Article
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.impl.CollectedArticlesOfUserImpl

interface CollectedArticlesOfUser {
    suspend fun collectsOfUser(userId: Long): DataResponse<List<Article>>

    companion object : CollectedArticlesOfUser by CollectedArticlesOfUserImpl()
}