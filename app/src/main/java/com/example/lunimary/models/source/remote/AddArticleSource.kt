package com.example.lunimary.models.source.remote

import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.DataResponse

interface AddArticleSource {

    suspend fun addArticle(article: Article): DataResponse<Unit>
}