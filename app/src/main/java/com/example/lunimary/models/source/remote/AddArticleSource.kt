package com.example.lunimary.models.source.remote

import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.DataResponse
import com.example.lunimary.models.source.remote.impl.AddArticleSourceImpl

interface AddArticleSource {

    suspend fun addArticle(article: Article): DataResponse<Unit>

    companion object : AddArticleSource by AddArticleSourceImpl()
}