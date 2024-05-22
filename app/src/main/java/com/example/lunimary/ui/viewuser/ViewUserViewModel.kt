package com.example.lunimary.ui.viewuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lunimary.base.pager.FlowPagingData
import com.example.lunimary.base.pager.pagerFlow
import com.example.lunimary.base.viewmodel.BaseViewModel
import com.example.lunimary.model.Article
import com.example.lunimary.model.User
import com.example.lunimary.model.source.remote.paging.UserPublicArticleSource
import com.example.lunimary.model.source.remote.repository.UserDetailRepository

class ViewUserViewModel : BaseViewModel() {
    private val userDetailRepository = UserDetailRepository()

    private val _uiState: MutableLiveData<UiState> = MutableLiveData(UiState())
    val uiState: LiveData<UiState> get() = _uiState

    private var user = User.NONE

    var userArticles: FlowPagingData<Article>? = null

    fun setUser(newUser: User) {
        if (user == User.NONE) {
            user = newUser
            interactionData()
            userArticles = pagerFlow { UserPublicArticleSource(newUser.id) }
        }
    }

    private fun interactionData() {
        request(
            block = {
                userDetailRepository.interactionData(user.id)
            },
            onSuccess = { data, _ ->
                data?.let {
                    _uiState.value = uiState.value!!.copy(
                        likeNum = it.likeNum,
                        followNum = it.followingNum,
                        followersNum = it.followerNum
                    )
                }
            }
        )
    }
}

data class UiState(
    val likeNum: Long = 0,
    val followNum: Long = 0,
    val followersNum: Long = 0
)