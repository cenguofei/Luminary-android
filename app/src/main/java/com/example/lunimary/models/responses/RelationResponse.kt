package com.example.lunimary.models.responses

import com.example.lunimary.base.BaseResponse
import com.example.lunimary.util.empty
import kotlinx.serialization.Serializable


@Serializable
class RelationResponse<T> : BaseResponse<RelationData<T>>() {
    /**
     * 朋友、关注、或粉丝的数量
     */
    fun copy(
        msg: String = empty,
        data: RelationData<T>? = null
    ): RelationResponse<T> {
        this.msg = msg
        this.data = data
        return this
    }
}

@Serializable
data class RelationData<T>(
    val num: Int = 0,
    val relations: List<T> = emptyList()
)