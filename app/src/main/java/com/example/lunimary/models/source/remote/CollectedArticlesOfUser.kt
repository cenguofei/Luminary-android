package com.example.lunimary.models.source.remote

import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.impl.CollectedArticlesOfUserImpl

interface CollectedArticlesOfUser {
    suspend fun collectsOfUser(userId: Long): DataResponse<List<Article>>

    companion object : CollectedArticlesOfUser by CollectedArticlesOfUserImpl()
}