package com.example.lunimary.model.responses

import com.example.lunimary.base.BaseResponse
import kotlinx.serialization.Serializable

/**
 * request:
 * http://localhost:8080/article/all?curPage=0&pageCount=12
 */
@Serializable
class PageResponse<T>: BaseResponse<Page<T>>()

@Serializable
data class Page<T>(
    /**
     * prevPage = curPage - 1
     *
     * nextPage = curPage + 1
     */
    val curPage: Int = 0,

    /**
     * 总页数
     */
    val pageSize: Long = 0,

    /**
     * 数据总量
     */
    val total: Long = 0,

    val lists: List<T>
)

fun <T> Page<T>?.isNotEmpty(): Boolean = this != null && lists.isNotEmpty()

const val DEFAULT_PER_PAGE_COUNT = 12