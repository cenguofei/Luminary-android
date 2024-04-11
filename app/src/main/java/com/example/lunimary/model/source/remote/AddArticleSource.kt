package com.example.lunimary.model.source.remote

import com.example.lunimary.model.Article
import com.example.lunimary.model.responses.DataResponse
import com.example.lunimary.model.source.remote.impl.AddArticleSourceImpl

interface AddArticleSource {

    suspend fun addArticle(article: Article): DataResponse<Unit>

    companion object : AddArticleSource by AddArticleSourceImpl()
}