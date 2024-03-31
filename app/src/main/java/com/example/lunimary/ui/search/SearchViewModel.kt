package com.example.lunimary.ui.search

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.pager.AppPagingSource
import com.example.lunimary.models.Article
import com.example.lunimary.models.User
import com.example.lunimary.models.source.remote.paging.SearchArticleSource
import com.example.lunimary.models.source.remote.paging.SearchUserSource
import com.example.lunimary.util.logi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class SearchViewModel : BaseViewModel() {
    private val _searchContent: MutableSharedFlow<String> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val searchContent: SharedFlow<String> get() = _searchContent

    private val _searchType: MutableSharedFlow<SearchType> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val searchType: SharedFlow<SearchType> get() = _searchType

    fun onSearchQueryChanged(searchContent: String) {
        viewModelScope.launch {
            _searchContent.emit(searchContent)
        }
    }

    fun onSearchTypeChanged(searchType: SearchType) {
        viewModelScope.launch {
            _searchType.emit(searchType)
        }
    }

    private var articleFirst = true

    @OptIn(FlowPreview::class)
    val searchArticles: Flow<PagingData<Article>> = channelFlow {
        searchContent.combine(searchType) { search, type ->
            search to type
        }.onEach {
            if (it.first.isNotBlank() && articleFirst) {
                articleFirst = false
            }
        }.debounce(500)
            .filter {
                val (search, type) = it
                type == SearchType.Article && (search.isNotBlank() || !articleFirst)
            }
            .map { AppPagingSource(SearchArticleSource(it.first)) }
            .collectLatest {
                "searchType.value == SearchType.Article".logi("search_test")
                Pager(PagingConfig(pageSize = 5)) { it }
                    .flow.cachedIn(viewModelScope)
                    .collectLatest { data ->
                        trySend(data)
                    }
            }
        awaitClose { close() }
    }

    private var userFirst = true

    @OptIn(FlowPreview::class)
    val searchUsers: Flow<PagingData<User>> = channelFlow {
        searchContent.combine(searchType) { search, type ->
            search to type
        }.onEach {
            if (it.first.isNotBlank() && userFirst) {
                userFirst = false
            }
        }.debounce(500)
            .filter {
                val (search, type) = it
                type == SearchType.User && (search.isNotBlank() || !userFirst)
            }
            .map { AppPagingSource(SearchUserSource(it.first)) }
            .collectLatest {
                "searchType.value == SearchType.User".logi("search_test")
                Pager(PagingConfig(pageSize = 5)) { it }.flow
                    .cachedIn(viewModelScope)
                    .collectLatest { data ->
                        trySend(data)
                    }
            }
        awaitClose { close() }
    }
}