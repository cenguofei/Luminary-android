package com.example.lunimary.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.currentUser
import com.example.lunimary.base.pager.pagerFlow
import com.example.lunimary.models.ext.InteractionData
import com.example.lunimary.models.source.remote.paging.UserCollectedArticleSource
import com.example.lunimary.models.source.remote.paging.UserLikedArticleSource
import com.example.lunimary.models.source.remote.paging.UserPrivacyArticleSource
import com.example.lunimary.models.source.remote.paging.UserPublicArticleSource
import com.example.lunimary.models.source.remote.repository.UserDetailRepository
import com.example.lunimary.util.logi

class UserDetailViewModel : BaseViewModel() {
    private val repository = UserDetailRepository()

    private val _uiState: MutableLiveData<InteractionData> = MutableLiveData(InteractionData())
    val uiState: LiveData<InteractionData> get() = _uiState

    fun requestData() { getUserInteractionData() }

    val publicArticles = pagerFlow { UserPublicArticleSource(currentUser.id) }

    val privacyArticles = pagerFlow { UserPrivacyArticleSource }

    val collectsOfUser = pagerFlow { UserCollectedArticleSource }

    val likesOfUser = pagerFlow { UserLikedArticleSource }

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