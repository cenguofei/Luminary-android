package com.example.lunimary.model.wanandroid

import com.example.lunimary.model.Article


@kotlinx.serialization.Serializable
data class WanandroidArticles(
    val articles: List<Article> = emptyList()
)
