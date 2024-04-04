package com.example.lunimary.ui.home

import androidx.paging.PagingData
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.pager.pagerFlow
import com.example.lunimary.models.Article
import com.example.lunimary.models.source.remote.paging.FriendsPageArticleSource
import com.example.lunimary.models.source.remote.paging.PageAllArticleSource
import com.example.lunimary.models.source.remote.paging.RecommendArticlesPageSource
import kotlinx.coroutines.flow.Flow

class RecommendViewModel : BaseViewModel() {

    private var _goToLogin = false
    val goToLogin: Boolean get() = _goToLogin
    fun updateGoToLogin(goToLogin: Boolean) {
        _goToLogin = goToLogin
    }

    val allArticles: Flow<PagingData<Article>> = pagerFlow { PageAllArticleSource }

    val friendsArticles: Flow<PagingData<Article>> = pagerFlow { FriendsPageArticleSource }

    val recommendArticles: Flow<PagingData<Article>> = pagerFlow { RecommendArticlesPageSource }
}