package com.example.lunimary.ui.viewuser

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.request
import com.example.lunimary.models.Article
import com.example.lunimary.models.User
import com.example.lunimary.models.source.remote.repository.ArticleRepository
import com.example.lunimary.models.source.remote.repository.UserDetailRepository
import com.example.lunimary.network.NetworkResult

class ViewUserViewModel : BaseViewModel() {
    private val articleRepository = ArticleRepository()
    private val userDetailRepository = UserDetailRepository()

    private val _userArticles: MutableLiveData<NetworkResult<List<Article>>> = MutableLiveData(NetworkResult.None())
    val userArticles: LiveData<NetworkResult<List<Article>>> get() = _userArticles

    private val _uiState: MutableState<UiState> = mutableStateOf(UiState())
    val uiState: State<UiState> get() = _uiState

    private fun publicArticles(userId: Long) {
        fly(FLY_VIEW_USER_PUBLIC_ARTICLES) {
            request(
                block = {
                    _userArticles.postValue(NetworkResult.Loading())
                    articleRepository.publicArticles(userId)
                },
                onSuccess = { data, _ ->
                    _userArticles.postValue(NetworkResult.Success(data))
                },
                onFailed = {
                    _userArticles.postValue(NetworkResult.Error(it))
                },
                onFinish = {
                    land(FLY_VIEW_USER_PUBLIC_ARTICLES)
                }
            )
        }
    }

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
    fun setUser(newUser: User) {
        if (user == User.NONE) {
            user = newUser
            publicArticles(user.id)
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

const val FLY_VIEW_USER_PUBLIC_ARTICLES = "fly_view_user_public_articles"
const val FLY_VIEW_USER_LIKES_OF_ARTICLES = "fly_view_user_likes_of_user_articles"
const val FLY_VIEW_USER_FOLLOWINGS = "fly_view_user_followings"
const val FLY_VIEW_USER_FOLLOWERS = "fly_view_user_followers"
