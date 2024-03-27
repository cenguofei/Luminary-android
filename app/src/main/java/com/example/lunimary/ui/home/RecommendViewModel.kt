package com.example.lunimary.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.models.Article
import com.example.lunimary.models.source.remote.paging.AppPagingSource
import com.example.lunimary.models.source.remote.paging.FriendsPageArticles
import com.example.lunimary.models.source.remote.paging.RecommendPageArticles
import kotlinx.coroutines.flow.Flow

class RecommendViewModel : BaseViewModel() {
    val recommendArticles: Flow<PagingData<Article>> = Pager(PagingConfig(pageSize = 5)) {
        AppPagingSource(RecommendPageArticles())
    }.flow.cachedIn(viewModelScope)

    val friendsArticles: Flow<PagingData<Article>> = Pager(PagingConfig(pageSize = 5)) {
        AppPagingSource(FriendsPageArticles())
    }.flow.cachedIn(viewModelScope)
}