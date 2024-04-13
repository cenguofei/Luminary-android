package com.example.lunimary.base.pager

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.lunimary.base.viewmodel.BaseViewModel
import com.example.lunimary.model.source.remote.paging.PageSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

val defaultPagingConfig: PagingConfig get() = PagingConfig(pageSize = 5)

/**
 * @param sourceFactory
 */
fun <T: Any> BaseViewModel.pagerFlow(
    sourceFactory: () -> PageSource<T>
) : FlowPagingData<T>  {
    return Pager(defaultPagingConfig) { AppPagingSource(sourceFactory()) }
        .flow.cachedIn(viewModelScope)
}

fun <T: Any> BaseViewModel.pagerMutableFlow(
    sourceFactory: () -> PageSource<T>
) : MutableStateFlow<PagingData<PageItem<T>>>  {
    val mutableStateFlow = MutableStateFlow(PagingData.empty<PageItem<T>>())
    viewModelScope.launch {
        Pager(defaultPagingConfig) { AppPagingSource(sourceFactory()) }
            .flow
            .cachedIn(viewModelScope)
            .collectLatest { mutableStateFlow.value = it }
    }
    return mutableStateFlow
}


typealias FlowPagingData<T> = Flow<PagingData<PageItem<T>>>