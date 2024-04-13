package com.example.lunimary.ui.user

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lunimary.base.currentUser
import com.example.lunimary.base.pager.FlowPagingData
import com.example.lunimary.base.pager.pagerFlow
import com.example.lunimary.base.pager.pagerState
import com.example.lunimary.base.viewmodel.BaseViewModel
import com.example.lunimary.model.Article
import com.example.lunimary.model.ext.InteractionData
import com.example.lunimary.model.source.remote.paging.UserCollectedArticleSource
import com.example.lunimary.model.source.remote.paging.UserLikedArticleSource
import com.example.lunimary.model.source.remote.paging.UserPrivacyArticleSource
import com.example.lunimary.model.source.remote.paging.UserPublicArticleSource
import com.example.lunimary.model.source.remote.repository.UserDetailRepository
import com.example.lunimary.util.logi

class UserDetailViewModel : BaseViewModel() {
    private val repository = UserDetailRepository()

    private val _uiState: MutableLiveData<InteractionData> = MutableLiveData(InteractionData())
    val uiState: LiveData<InteractionData> get() = _uiState

    val tabs = listOf(
        ArticlesType.Composition, ArticlesType.Privacy,
        ArticlesType.Collect, ArticlesType.Like
    )

    @OptIn(ExperimentalFoundationApi::class)
    val pagerState = pagerState { tabs.size }

    fun requestData() {
        getUserInteractionData()
    }

    private var _publicArticles = pagerFlow { UserPublicArticleSource(currentUser.id) }
    val publicArticles: FlowPagingData<Article> get() = _publicArticles

    private var _privacyArticles = pagerFlow { UserPrivacyArticleSource }
    val privacyArticles: FlowPagingData<Article> get() = _privacyArticles

    private var _collectsOfUser = pagerFlow { UserCollectedArticleSource }
    val collectsOfUser: FlowPagingData<Article> get() = _collectsOfUser

    private var _likesOfUser = pagerFlow { UserLikedArticleSource }
    val likesOfUser: FlowPagingData<Article> get() = _likesOfUser

    fun resetPagerFlows() {
        _publicArticles = pagerFlow { UserPublicArticleSource(currentUser.id) }
        _privacyArticles = pagerFlow { UserPrivacyArticleSource }
        _collectsOfUser = pagerFlow { UserCollectedArticleSource }
        _likesOfUser = pagerFlow { UserLikedArticleSource }
    }

    private fun getUserInteractionData() {
        "getUserInteractionData".logi("getUserInteractionData")
        fly(FLY_GET_USER_INTERACTION_DATA) {
            request(
                block = { repository.interactionData() },
                onSuccess = { data, _ ->
                    "getUserInteractionData data=$data".logi("getUserInteractionData")
                    data?.let { _uiState.postValue(it) }
                },
                onFinish = { land(FLY_GET_USER_INTERACTION_DATA) }
            )
        }
    }
}

const val FLY_GET_USER_INTERACTION_DATA = "fly_get_user_interaction_data"