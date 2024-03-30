package com.example.lunimary.ui.home

import androidx.paging.PagingData
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.pager.pagerFlow
import com.example.lunimary.models.Article
import com.example.lunimary.models.source.remote.paging.FriendsPageArticleSource
import com.example.lunimary.models.source.remote.paging.RecommendPageArticleSource
import kotlinx.coroutines.flow.Flow

class RecommendViewModel : BaseViewModel() {
    val recommendArticles: Flow<PagingData<Article>> = pagerFlow { RecommendPageArticleSource() }

    val friendsArticles: Flow<PagingData<Article>> = pagerFlow { FriendsPageArticleSource() }
}