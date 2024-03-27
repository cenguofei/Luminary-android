package com.example.lunimary.ui.search

sealed interface SearchResult {
    object None : SearchResult

    object Empty : SearchResult

    data class Failed(val e:Throwable?) : SearchResult

    data class Success<T> (
        val result: List<T> = emptyList()
    ) : SearchResult
}