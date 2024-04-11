package com.example.lunimary.base.pager

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.model.source.remote.paging.PageSource
import kotlinx.coroutines.flow.Flow

val defaultPagingConfig: PagingConfig get() = PagingConfig(pageSize = 5)

/**
 * @param sourceFactory
 */
fun <T: Any> BaseViewModel.pagerFlow(
    sourceFactory: () -> PageSource<T>
) : Flow<PagingData<PageItem<T>>>  {
    return Pager(defaultPagingConfig) { AppPagingSource(sourceFactory()) }
        .flow.cachedIn(viewModelScope)
}