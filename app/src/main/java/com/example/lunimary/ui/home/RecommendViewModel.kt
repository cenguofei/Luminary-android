package com.example.lunimary.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.request
import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.Page
import com.example.lunimary.models.source.remote.repository.ArticleRepository
import com.example.lunimary.models.source.remote.paging.ArticlePagingSource
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.util.logd
import kotlinx.coroutines.flow.Flow

class RecommendViewModel : BaseViewModel() {
    private val articleRepository = ArticleRepository()

    private val _pageArticles: MutableLiveData<NetworkResult<Page<Article>>> = MutableLiveData()
    val pageArticles: LiveData<NetworkResult<Page<Article>>> get() = _pageArticles

    val articles: Flow<PagingData<Article>> = Pager(PagingConfig(pageSize = 5)) {
        ArticlePagingSource()
    }.flow.cachedIn(viewModelScope)

    fun recommendedArticles() {
//        request(
//            block = {
//                _pageArticles.postValue(NetworkResult.Loading())
//                articleRepository.recommendedArticles(0, 30)
//            },
//            onSuccess = { data, _ ->
//                if (data == null || data.isEmpty()) {
//                    _pageArticles.postValue(NetworkResult.Empty())
//                } else {
//                    _pageArticles.postValue(NetworkResult.Success(data))
//                }
//            },
//            onFailed = {
//                _pageArticles.postValue(NetworkResult.Error(it))
//            }
//        )
    }

    fun getArticleById() {
        "获取文章：getArticleById".logd("home_d")
        request(
            block = {
                articleRepository.getArticleById(58)
            },
            onSuccess = { data, msg ->

            }
        )
    }
}