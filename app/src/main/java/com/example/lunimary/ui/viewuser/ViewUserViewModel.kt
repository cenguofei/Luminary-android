package com.example.lunimary.ui.viewuser

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.pager.pagerFlow
import com.example.lunimary.models.User
import com.example.lunimary.models.source.remote.paging.UserPublicArticleSource
import com.example.lunimary.models.source.remote.repository.UserDetailRepository

class ViewUserViewModel : BaseViewModel() {
    private val userDetailRepository = UserDetailRepository()

    private val _uiState: MutableState<UiState> = mutableStateOf(UiState())
    val uiState: State<UiState> get() = _uiState

    private fun likesOfUserArticles(userId: Long) {
        fly(FLY_VIEW_USER_LIKES_OF_ARTICLES) {
            request(
                block = {
                    userDetailRepository.likesOfUser(userId)
                },
                onSuccess = { data, _ ->
                    if (data != null) {
                        _uiState.value = uiState.value.copy(likeNum = data)
                    }
                },
                onFinish = {
                    land(FLY_VIEW_USER_LIKES_OF_ARTICLES)
                }
            )
        }
    }

    private fun followings(userId: Long) {
        fly(FLY_VIEW_USER_FOLLOWINGS) {
            request(
                block = {
                    userDetailRepository.followings(userId)
                },
                onSuccess = { data, _ ->
                    data?.let {
                        _uiState.value = uiState.value.copy(followNum = it.num)
                    }
                },
                onFinish = { land(FLY_VIEW_USER_FOLLOWINGS) }
            )
        }
    }

    private fun followers(userId: Long) {
        fly(FLY_VIEW_USER_FOLLOWERS) {
            request(
                block = {
                    userDetailRepository.followers(userId)
                },
                onSuccess = { data, _ ->
                    data?.let {
                        _uiState.value = uiState.value.copy(followersNum = it.num)
                    }
                },
                onFailed = { },
                onFinish = { land(FLY_VIEW_USER_FOLLOWERS) }
            )
        }
    }

    private var user = User.NONE
    private val userId: Long get() = if (user != User.NONE) user.id else -1

    val userArticles = pagerFlow { UserPublicArticleSource(userId) }
    fun setUser(newUser: User) {
        if (user == User.NONE) {
            user = newUser
            likesOfUserArticles(user.id)
            followers(user.id)
            followings(user.id)
        }
    }
}

data class UiState(
    val likeNum: Long = 0,
    val followNum: Int = 0,
    val followersNum: Int = 0
)

const val FLY_VIEW_USER_LIKES_OF_ARTICLES = "fly_view_user_likes_of_user_articles"
const val FLY_VIEW_USER_FOLLOWINGS = "fly_view_user_followings"
const val FLY_VIEW_USER_FOLLOWERS = "fly_view_user_followers"
