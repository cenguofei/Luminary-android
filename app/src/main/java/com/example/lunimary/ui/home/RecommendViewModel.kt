package com.example.lunimary.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.request
import com.example.lunimary.models.Article
import com.example.lunimary.models.responses.Page
import com.example.lunimary.models.responses.isEmpty
import com.example.lunimary.models.source.remote.ArticleRepository
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.util.logd

class RecommendViewModel : BaseViewModel() {
    private val articleRepository = ArticleRepository()

    private val _pageArticles: MutableLiveData<NetworkResult<Page<Article>>> = MutableLiveData()
    val pageArticles: LiveData<NetworkResult<Page<Article>>> get() = _pageArticles

    fun recommendedArticles() {
        request(
            block = {
                _pageArticles.postValue(NetworkResult.Loading())
                articleRepository.recommendedArticles(0, 30)
            },
            onSuccess = { data, _ ->
                if (data == null || data.isEmpty()) {
                    _pageArticles.postValue(NetworkResult.Empty())
                } else {
                    _pageArticles.postValue(NetworkResult.Success(data))
                }
            },
            onFailed = {
                _pageArticles.postValue(NetworkResult.Error(it))
            }
        )
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