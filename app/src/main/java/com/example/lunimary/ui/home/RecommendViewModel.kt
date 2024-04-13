package com.example.lunimary.ui.home

import com.example.lunimary.base.pager.FlowPagingData
import com.example.lunimary.base.pager.pagerFlow
import com.example.lunimary.base.pager.pagerMutableFlow
import com.example.lunimary.base.viewmodel.BaseViewModel
import com.example.lunimary.model.Article
import com.example.lunimary.model.source.remote.paging.FriendsPageArticleSource
import com.example.lunimary.model.source.remote.paging.PageAllArticleSource
import com.example.lunimary.model.source.remote.paging.RecommendArticlesPageSource

class RecommendViewModel : BaseViewModel() {
    private var _allArticles = pagerFlow { PageAllArticleSource }
    val allArticles: FlowPagingData<Article> get() = _allArticles

    private var _friendsArticles = pagerFlow { FriendsPageArticleSource }
    val friendsArticles: FlowPagingData<Article> get() = _friendsArticles

    private var _recommendArticles = pagerFlow { RecommendArticlesPageSource }
    val recommendArticles: FlowPagingData<Article> get() = _recommendArticles

    fun resetPagerFlows() {
        _allArticles = pagerMutableFlow { PageAllArticleSource }
        _friendsArticles = pagerMutableFlow { FriendsPageArticleSource }
        _recommendArticles = pagerMutableFlow { RecommendArticlesPageSource }
    }
}