package com.example.lunimary.models.responses

import com.example.lunimary.base.BaseResponse
import kotlinx.serialization.Serializable


@Serializable
class RelationResponse<T> : BaseResponse<RelationData<T>>()
@Serializable
data class RelationData<T>(
    val num: Int = 0,
    val relations: List<T> = emptyList()
)