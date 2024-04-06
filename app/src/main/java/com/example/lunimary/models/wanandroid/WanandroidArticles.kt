package com.example.lunimary.models.wanandroid

import com.example.lunimary.models.Article


@kotlinx.serialization.Serializable
data class WanandroidArticles(
    val articles: List<Article> = emptyList()
)
