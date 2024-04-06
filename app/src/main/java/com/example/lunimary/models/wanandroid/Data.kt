package com.example.lunimary.models.wanandroid

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val curPage: Int,
    val datas: List<DataX>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)