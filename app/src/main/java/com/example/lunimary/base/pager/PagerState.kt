package com.example.lunimary.base.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.mutableStateOf

/**
 * �ǿ����PagerState��ʹViewModel�ܱ����״̬
 */
@OptIn(ExperimentalFoundationApi::class)
fun pagerState(
    initialPage: Int = 0,
    initialPageOffsetFraction: Float = 0f,
    pageCount: () -> Int
): PagerState = PagerStateImpl(
    initialPage = initialPage,
    initialPageOffsetFraction = initialPageOffsetFraction,
    updatedPageCount = pageCount
).apply { pageCountState.value = pageCount }

@OptIn(ExperimentalFoundationApi::class)
private class PagerStateImpl(
    initialPage: Int,
    initialPageOffsetFraction: Float,
    updatedPageCount: () -> Int
) : PagerState(initialPage, initialPageOffsetFraction) {

    var pageCountState = mutableStateOf(updatedPageCount)
    override val pageCount: Int get() = pageCountState.value.invoke()
}