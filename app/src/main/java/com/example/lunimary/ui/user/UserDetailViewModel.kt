package com.example.lunimary.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.currentUser
import com.example.lunimary.base.pager.pagerFlow
import com.example.lunimary.models.Article
import com.example.lunimary.models.source.remote.paging.UserCollectedArticleSource
import com.example.lunimary.models.source.remote.paging.UserLikedArticleSource
import com.example.lunimary.models.source.remote.paging.UserPrivacyArticleSource
import com.example.lunimary.models.source.remote.paging.UserPublicArticleSource
import com.example.lunimary.models.source.remote.repository.UserDetailRepository
import com.example.lunimary.util.empty

class UserDetailViewModel : BaseViewModel() {
    private val repository = UserDetailRepository()

    private val _uiState: MutableLiveData<UserUiState> = MutableLiveData(UserUiState())
    val uiState: LiveData<UserUiState> get() = _uiState

    fun requestData() {
        likesOfUserArticles()
        followers()
        followings()
        mutualFollowUsers()
    }
    val publicArticles = pagerFlow { UserPublicArticleSource(currentUser.id) }

    val privacyArticles = pagerFlow { UserPrivacyArticleSource }

    val collectsOfUser = pagerFlow { UserCollectedArticleSource }

    val likesOfUser = pagerFlow { UserLikedArticleSource }

    private fun likesOfUserArticles() {
        fly(FLY_LIKES_OF_USER_ARTICLES) {
            request(
                block = {
                    repository.likesOfUser(currentUser.id)
                },
                onSuccess = { data, _ ->
                    if (data != null) {
                        _uiState.postValue(uiState.value?.copy(likesOfUserArticles = data))
                    }
                },
                onFinish = {
                    land(FLY_LIKES_OF_USER_ARTICLES)
                }
            )
        }
    }

    private fun followings() {
        fly(FLY_FOLLOWINGS) {
            request(
                block = {
                    repository.followings(currentUser.id)
                },
                onSuccess = { data, _ ->
                    data?.let {
                        _uiState.postValue(uiState.value?.copy(followingNum = it.num))
                    }
                },
                onFinish = { land(FLY_FOLLOWINGS) }
            )
        }
    }

    private fun followers() {
        fly(FLY_FOLLOWERS) {
            request(
                block = {
                    repository.followers(currentUser.id)
                },
                onSuccess = { data, _ ->
                    data?.let {
                        _uiState.postValue(uiState.value?.copy(followersNum = it.num))
                    }
                },
                onFailed = { },
                onFinish = { land(FLY_FOLLOWERS) }
            )
        }
    }

    private fun mutualFollowUsers() {
        fly(FLY_MUTUAL_FOLLOW_USERS) {
            request(
                block = {
                    repository.mutualFollowUsers()
                },
                onSuccess = { data, _ ->
                    data?.let {
                        _uiState.postValue(uiState.value?.copy(friendsNum = it.num))
                    }
                },
                onFailed = { },
                onFinish = { land(FLY_MUTUAL_FOLLOW_USERS) }
            )
        }
    }
}

data class UserUiState(
    val likesOfUserArticles: Long = 0,
    val followingNum:Int = 0,
    val followersNum: Int = 0,
    val articlesOfUser: List<Article> = emptyList(),
    val errorOfGetArticlesOfUser: String = empty,
    val friendsNum: Int = 0
)

const val FLY_LIKES_OF_USER_ARTICLES = "fly_likes_of_user_articles"
const val FLY_FOLLOWINGS = "fly_followings"
const val FLY_FOLLOWERS = "fly_followers"
const val FLY_MUTUAL_FOLLOW_USERS = "fly_mutual_follow_users"