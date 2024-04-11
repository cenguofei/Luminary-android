package com.example.lunimary.ui.home

import androidx.paging.PagingData
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.base.pager.pagerFlow
import com.example.lunimary.model.Article
import com.example.lunimary.model.source.remote.paging.FriendsPageArticleSource
import com.example.lunimary.model.source.remote.paging.PageAllArticleSource
import com.example.lunimary.model.source.remote.paging.RecommendArticlesPageSource
import kotlinx.coroutines.flow.Flow

class RecommendViewModel : BaseViewModel() {
    val allArticles: Flow<PagingData<PageItem<Article>>> = pagerFlow { PageAllArticleSource }

    val friendsArticles: Flow<PagingData<PageItem<Article>>> = pagerFlow { FriendsPageArticleSource }

    val recommendArticles: Flow<PagingData<PageItem<Article>>> = pagerFlow { RecommendArticlesPageSource }
}