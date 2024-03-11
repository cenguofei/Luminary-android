package com.example.lunimary.models.source.remote

import com.example.lunimary.models.Article
import com.example.lunimary.models.Collect
import com.example.lunimary.models.responses.DataResponse

interface CollectSource {
    suspend fun collectsOfUser(userId: Long): DataResponse<List<Article>>
}