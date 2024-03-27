package com.example.lunimary.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.request
import com.example.lunimary.models.Article
import com.example.lunimary.models.source.remote.repository.ArticleRepository
import com.example.lunimary.models.source.remote.repository.ArticlesOfUserLikeRepository
import com.example.lunimary.models.source.remote.repository.UserDetailRepository
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.util.currentUser
import com.example.lunimary.util.empty

class UserDetailViewModel : BaseViewModel() {
    private val repository = UserDetailRepository()
    private val articleRepository = ArticleRepository()
    private val articlesOfUserLikeRepository = ArticlesOfUserLikeRepository()

    private val _uiState: MutableLiveData<UserUiState> = MutableLiveData(UserUiState())
    val uiState: LiveData<UserUiState> get() = _uiState

    fun requestData() {
        likesOfUserArticles()
        followers()
        followings()
        mutualFollowUsers()
    }

    private val _publicArticles: MutableLiveData<NetworkResult<List<Article>>> =
        MutableLiveData(NetworkResult.None())
    val publicArticles: LiveData<NetworkResult<List<Article>>> get() = _publicArticles
    fun publicArticles() {
        fly(FLY_PUBLIC_ARTICLES_OF_USER) {
            request(
                block = {
                    _publicArticles.postValue(NetworkResult.Loading())
                    articleRepository.publicArticles(currentUser.id)
                },
                onSuccess = { data, _ ->
                    _publicArticles.postValue(NetworkResult.Success(data))
                },
                onFailed = {
                    _publicArticles.postValue(NetworkResult.Error(it))
                },
                onFinish = {
                    land(FLY_PUBLIC_ARTICLES_OF_USER)
                }
            )
        }
    }

    private val _privacyArticles: MutableLiveData<NetworkResult<List<Article>>> =
        MutableLiveData(NetworkResult.None())
    val privacyArticles: LiveData<NetworkResult<List<Article>>> get() = _privacyArticles
    fun privacyArticles() {
        fly(FLY_PRIVACY_ARTICLES_OF_USER) {
            request(
                block = {
                    _privacyArticles.postValue(NetworkResult.Loading())
                    articleRepository.privacyArticles(currentUser.id)
                },
                onSuccess = { data, _ ->
                    _privacyArticles.postValue(NetworkResult.Success(data))
                },
                onFailed = {
                    _privacyArticles.postValue(NetworkResult.Error(it))
                },
                onFinish = {
                    land(FLY_PRIVACY_ARTICLES_OF_USER)
                }
            )
        }
    }

    private val _collectsOfUser: MutableLiveData<NetworkResult<List<Article>>> =
        MutableLiveData(NetworkResult.None())
    val collectsOfUser: LiveData<NetworkResult<List<Article>>> get() = _collectsOfUser
    fun collectsOfUser() {
        fly(FLY_COLLECTS_OF_USER) {
            request(
                block = {
                    _collectsOfUser.postValue(NetworkResult.Loading())
                    repository.collectsOfUser(currentUser.id)
                },
                onSuccess = { data, msg ->
                    if (data.isNullOrEmpty()) {
                        _collectsOfUser.postValue(NetworkResult.Empty(msg ?: empty))
                    } else {
                        _collectsOfUser.postValue(NetworkResult.Success(data))
                    }
                },
                onFailed = {
                    _collectsOfUser.postValue(NetworkResult.Error(it))
                },
                onFinish = {
                    land(FLY_COLLECTS_OF_USER)
                }
            )
        }
    }

    private val _likesOfUser: MutableLiveData<NetworkResult<List<Article>>> =
        MutableLiveData(NetworkResult.None())
    val likesOfUser: LiveData<NetworkResult<List<Article>>> get() = _likesOfUser
    fun likesOfUser() {
        fly(FLY_LIKES_OF_USER) {
            request(
                block = {
                    _likesOfUser.postValue(NetworkResult.Loading())
                    articlesOfUserLikeRepository.likesOfUser(currentUser.id)
                },
                onSuccess = { data, msg ->
                    if (data.isNullOrEmpty()) {
                        _likesOfUser.postValue(NetworkResult.Empty(msg ?: empty))
                    } else {
                        _likesOfUser.postValue(NetworkResult.Success(data))
                    }
                },
                onFailed = {
                    _likesOfUser.postValue(NetworkResult.Error(it))
                },
                onFinish = {
                    land(FLY_LIKES_OF_USER)
                }
            )
        }
    }

    private fun likesOfUserArticles() {
        fly(FLY_LIKES_OF_USER_ARTICLES) {
            request(
                block = {
                    repository.likesOfUser()
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
                    repository.followings()
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
                    repository.followers()
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
const val FLY_PUBLIC_ARTICLES_OF_USER = "fly_public_articles_of_user"
const val FLY_PRIVACY_ARTICLES_OF_USER = "fly_privacy_articles_of_user"
const val FLY_COLLECTS_OF_USER = "fly_articles_of_user_collected"
const val FLY_LIKES_OF_USER = "fly_articles_of_user_liked"
const val FLY_MUTUAL_FOLLOW_USERS = "fly_mutual_follow_users"