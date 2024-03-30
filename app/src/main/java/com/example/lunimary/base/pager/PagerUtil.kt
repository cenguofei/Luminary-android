package com.example.lunimary.base.pager

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.models.source.remote.PageSource
import kotlinx.coroutines.flow.Flow

val defaultPagingConfig: PagingConfig get() = PagingConfig(pageSize = 5)

fun <T: Any> BaseViewModel.pagerFlow(
    sourceFactory: () -> PageSource<T>
) : Flow<PagingData<T>>  {
    return Pager(defaultPagingConfig) {
        AppPagingSource(sourceFactory())
    }.flow.cachedIn(viewModelScope)
}