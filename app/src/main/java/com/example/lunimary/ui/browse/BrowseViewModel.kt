package com.example.lunimary.ui.browse

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.request
import com.example.lunimary.models.Article
import com.example.lunimary.models.Comment
import com.example.lunimary.models.User
import com.example.lunimary.models.responses.CombinedCommentMessage
import com.example.lunimary.models.source.remote.repository.CollectRepository
import com.example.lunimary.models.source.remote.repository.CommentRepository
import com.example.lunimary.models.source.remote.repository.FriendRepository
import com.example.lunimary.models.source.remote.repository.LikeRepository
import com.example.lunimary.models.source.remote.repository.UserRepository
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.util.currentUser

class BrowseViewModel : BaseViewModel() {
    private val userRepository = UserRepository()
    private val friendRepository = FriendRepository()
    private val likeRepository = LikeRepository()
    private val collectRepository = CollectRepository()
    private val commentRepository by lazy { CommentRepository() }

    private var hasSetArticle = false
    fun setArticle(article: Article) {
        if (hasSetArticle) return
        hasSetArticle = true
        _uiState.postValue(uiState.value?.copy(article = article))
        fetchUser(article.userId)
        existingFriendship(article.userId)
        existsLike(article.id)
        fetchStar(article.id)
        getAllCommentsOfArticle(article.id)
    }

    private fun existingFriendship(whoId: Long) {
        fly(FLY_EXISTING_FRIENDSHIP) {
            request(
                block = {
                    friendRepository.existingFriendship(currentUser.id, whoId)
                },
                onSuccess = { data, _ ->
                    val newUiState = uiState.value?.copy(hasFetchedFriendship = true)
                    if (data != null) {
                        _uiState.postValue(
                            newUiState?.copy(isFollowByMe = data.exists)
                        )
                    }
                },
                onFinish = { land(FLY_EXISTING_FRIENDSHIP) }
            )
        }
    }

    private val _articleOwner: MutableState<User> = mutableStateOf(User.NONE)
    val articleOwner: State<User> get() = _articleOwner

    private fun fetchUser(userId: Long) {
        fly(FLY_FETCH_USER) {
            request(
                block = { userRepository.queryUser(userId) },
                onSuccess = { data, _ ->
                    if (data?.user != null) {
                        _articleOwner.value = data.user
                    }
                },
                onFinish = { land(FLY_FETCH_USER) }
            )
        }
    }

    private val _uiState: MutableLiveData<UiState> = MutableLiveData(UiState())
    val uiState: LiveData<UiState> get() = _uiState

    fun onFollowClick() {
        fly(FLY_FOLLOW_OR_UNFOLLOW) {
            request(
                block = {
                    friendRepository.follow(currentUser.id, articleOwner.value.id)
                },
                onSuccess = { _, _ ->
                    _uiState.postValue(uiState.value?.copy(isFollowByMe = true))
                },
                onFinish = { land(FLY_FOLLOW_OR_UNFOLLOW) }
            )
        }
    }

    fun onUnfollowClick() {
        fly(FLY_FOLLOW_OR_UNFOLLOW) {
            request(
                block = {
                    friendRepository.unfollow(articleOwner.value.id)
                },
                onSuccess = { _, _ ->
                    _uiState.postValue(uiState.value?.copy(isFollowByMe = false))
                },
                onFinish = { land(FLY_FOLLOW_OR_UNFOLLOW) }
            )
        }
    }

    fun onGiveLike() {
        fly(FLy_ABOUT_LIKE) {
            request(
                block = {
                    likeRepository.giveLike(currentUser.id, uiState.value!!.article.id)
                },
                onSuccess = { _, _ ->
                    _likedTheArticle.value = true
                },
                onFinish = { land(FLy_ABOUT_LIKE) }
            )
        }
    }

    fun onCancelLike() {
        fly(FLy_ABOUT_LIKE) {
            request(
                block = {
                    likeRepository.cancelLike(currentUser.id, uiState.value!!.article.id)
                },
                onSuccess = { _, _ ->
                    _likedTheArticle.value = false
                },
                onFinish = { land(FLy_ABOUT_LIKE) }
            )
        }
    }

    private val _likedTheArticle: MutableState<Boolean> = mutableStateOf(false)
    val likedTheArticle: State<Boolean> get() = _likedTheArticle

    private fun existsLike(articleId: Long) {
        fly(FLy_ABOUT_LIKE) {
            request(
                block = {
                    likeRepository.existsLike(userId = currentUser.id, articleId = articleId)
                },
                onSuccess = { data, _ ->
                    _likedTheArticle.value = data ?: false
                },
                onFinish = { land(FLy_ABOUT_LIKE) }
            )
        }
    }

    private val _staredTheArticle: MutableState<Boolean> = mutableStateOf(false)
    val staredTheArticle: State<Boolean> get() = _staredTheArticle

    private fun fetchStar(articleId: Long) {
        fly(FLY_ABOUT_STAR) {
            request(
                block = {
                    collectRepository.existsCollect(
                        collectUserId = currentUser.id,
                        articleId = articleId
                    )
                },
                onSuccess = { data, _ ->
                    _staredTheArticle.value = data ?: false
                },
                onFinish = { land(FLY_ABOUT_STAR) }
            )
        }
    }

    fun onGiveStar() {
        fly(FLY_ABOUT_STAR) {
            request(
                block = {
                    collectRepository.giveCollect(currentUser.id, uiState.value!!.article.id)
                },
                onSuccess = { _, _ ->
                    _staredTheArticle.value = true
                },
                onFinish = { land(FLY_ABOUT_STAR) }
            )
        }
    }

    fun onCancelStar() {
        fly(FLY_ABOUT_STAR) {
            request(
                block = {
                    collectRepository.cancelCollect(currentUser.id, uiState.value!!.article.id)
                },
                onSuccess = { _, _ ->
                    _staredTheArticle.value = false
                },
                onFinish = { land(FLY_ABOUT_STAR) }
            )
        }
    }

    fun send(comment: String) {
        fly(FLY_CREATE_COMMENT) {
            request(
                block = {
                    commentRepository.createComment(
                        content = comment,
                        userId = currentUser.id,
                        articleId = uiState.value!!.article.id
                    )
                },
                onSuccess = { _, _ ->
                    getAllCommentsOfArticle(uiState.value!!.article.id)
                },
                onFailed = { },
                onFinish = { land(FLY_CREATE_COMMENT) }
            )
        }
    }

    private val _comments: MutableState<NetworkResult<CombinedCommentMessage>> = mutableStateOf(NetworkResult.None())
    val comments: State<NetworkResult<CombinedCommentMessage>> get() = _comments
    private fun getAllCommentsOfArticle(articleId: Long) {
        fly(FLY_ALL_COMMENTS_OF_ARTICLE) {
            request(
                block = {
                    _comments.value = NetworkResult.Loading()
                    commentRepository.getAllCommentsOfArticle(articleId)
                },
                onSuccess = { data, _ ->
                    _comments.value = NetworkResult.Success(data = data)
                },
                onFailed = {
                    _comments.value = NetworkResult.Error(it)
                },
                onFinish = { land(FLY_ALL_COMMENTS_OF_ARTICLE) }
            )
        }
    }

    fun transform(data: CombinedCommentMessage) : List<Pair<User, Comment>> {
        val flatComments = mutableListOf<Pair<User, Comment>>()
        data.forEach { combined ->
            if (combined.user != null) {
                combined.messages.forEach {
                    flatComments += combined.user to it
                }
            }
        }
        flatComments.sortBy { it.second.timestamp }
        return flatComments
    }
}

data class UiState(
    val article: Article = Article.Default,
    val isFollowByMe: Boolean = false,
    /**
     * 是否已经查询到用户和文章所属用户的关系
     */
    val hasFetchedFriendship: Boolean = false,
)

const val FLY_FETCH_USER = "fly_fetch_user"
const val FLY_EXISTING_FRIENDSHIP = "fly_existing_friendship"
const val FLY_FOLLOW_OR_UNFOLLOW = "fly_follow_or_unfollow"
const val FLY_ABOUT_STAR = "____fly_about_star____"
const val FLy_ABOUT_LIKE = "____fly_about_like____"
const val FLY_GIVE_OR_CANCEL_COLLECT = "fly_give_or_cancel_collect"
const val FLY_CREATE_COMMENT = "fly_create_comment"
const val FLY_ALL_COMMENTS_OF_ARTICLE = "fly_all_comments_of_article"