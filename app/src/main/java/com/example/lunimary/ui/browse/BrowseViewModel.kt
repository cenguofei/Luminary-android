package com.example.lunimary.ui.browse

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.lunimary.LunimaryApplication
import com.example.lunimary.base.DataState
import com.example.lunimary.base.currentUser
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.viewmodel.BaseViewModel
import com.example.lunimary.model.Article
import com.example.lunimary.model.Comment
import com.example.lunimary.model.User
import com.example.lunimary.model.ViewDurationTemp
import com.example.lunimary.model.VisibleMode
import com.example.lunimary.model.ext.CommentsWithUser
import com.example.lunimary.model.source.remote.repository.ArticleRepository
import com.example.lunimary.model.source.remote.repository.CollectRepository
import com.example.lunimary.model.source.remote.repository.CommentRepository
import com.example.lunimary.model.source.remote.repository.FriendRepository
import com.example.lunimary.model.source.remote.repository.LikeRepository
import com.example.lunimary.model.source.remote.repository.RecordViewDurationRepository
import com.example.lunimary.model.source.remote.repository.UserRepository
import com.example.lunimary.util.Default
import com.example.lunimary.util.logi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BrowseViewModel : BaseViewModel() {
    private val userRepository = UserRepository()
    private val friendRepository = FriendRepository()
    private val likeRepository = LikeRepository()
    private val collectRepository = CollectRepository()
    private val articleRepository = ArticleRepository()
    private val commentRepository by lazy { CommentRepository() }
    private val recordRepository = RecordViewDurationRepository()

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    private var beginTimestamp = Long.Default
    fun setArticle(article: Article) {
        _uiState.value = uiState.value.copy(article = article)
        existingArticle(article.id)
        fetchUser(article.userId)
        existingFriendship(article.userId)
        existsLike(article.id)
        fetchStar(article.id)
        whenBroseArticle(article.id)
        getAllCommentsOfArticle(article.id)
    }

    private fun existingArticle(articleId: Long) {
        request(
            block = {
                articleRepository.existing(articleId)
            },
            onSuccess = { existing, _ ->
                if (existing == false) {
                    _uiState.value = uiState.value.copy(articleDeleted = true)
                }
            }
        )
    }

    fun beginRecord() {
        "beginRecord".logi("begin_record_time")
        beginTimestamp = System.currentTimeMillis()
    }

    fun endRecord() {
        "endRecord".logi("begin_record_time")
        val duration = System.currentTimeMillis() - beginTimestamp
        val tags = uiState.value.article.tags.toList()
        if (duration >= 5000) {
            request(
                block = {
                    recordRepository.record(
                        ViewDurationTemp(
                            userId = currentUser.id,
                            duration = duration,
                            tags = tags,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
            )
        }
    }

    private fun whenBroseArticle(articleId: Long) {
        request { articleRepository.whenBrowseArticle(articleId) }
    }

    private fun existingFriendship(whoId: Long) {
        fly(FLY_EXISTING_FRIENDSHIP) {
            request(
                block = {
                    friendRepository.existingFriendship(currentUser.id, whoId)
                },
                onSuccess = { data, _ ->
                    val newUiState = uiState.value.copy(hasFetchedFriendship = true)
                    if (data != null) {
                        _uiState.value = newUiState.copy(isFollowByMe = data.exists)
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

    fun onFollowClick() {
        fly(FLY_FOLLOW_OR_UNFOLLOW) {
            request(
                block = {
                    friendRepository.follow(currentUser.id, articleOwner.value.id)
                },
                onSuccess = { _, _ ->
                    _uiState.value = uiState.value.copy(isFollowByMe = true)
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
                    _uiState.value = uiState.value.copy(isFollowByMe = false)
                },
                onFinish = { land(FLY_FOLLOW_OR_UNFOLLOW) }
            )
        }
    }

    fun onGiveLike() {
        fly(FLY_ABOUT_LIKE) {
            request(
                block = {
                    likeRepository.giveLike(currentUser.id, uiState.value.article.id)
                },
                onSuccess = { _, _ ->
                    _likedTheArticle.value = true
                },
                onFinish = { land(FLY_ABOUT_LIKE) }
            )
        }
    }

    fun onCancelLike() {
        fly(FLY_ABOUT_LIKE) {
            request(
                block = {
                    likeRepository.cancelLike(currentUser.id, uiState.value.article.id)
                },
                onSuccess = { _, _ ->
                    _likedTheArticle.value = false
                },
                onFinish = { land(FLY_ABOUT_LIKE) }
            )
        }
    }

    private val _likedTheArticle: MutableState<Boolean> = mutableStateOf(false)
    val likedTheArticle: State<Boolean> get() = _likedTheArticle

    private fun existsLike(articleId: Long) {
        fly(FLY_ABOUT_LIKE) {
            request(
                block = {
                    likeRepository.existsLike(userId = currentUser.id, articleId = articleId)
                },
                onSuccess = { data, _ ->
                    _likedTheArticle.value = data ?: false
                },
                onFinish = { land(FLY_ABOUT_LIKE) }
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
                    collectRepository.giveCollect(currentUser.id, uiState.value.article.id)
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
                    collectRepository.cancelCollect(currentUser.id, uiState.value.article.id)
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
                        articleId = uiState.value.article.id
                    )
                },
                onSuccess = { _, _ ->
                    getAllCommentsOfArticle(uiState.value.article.id)
                },
                onFailed = { },
                onFinish = { land(FLY_CREATE_COMMENT) }
            )
        }
    }

    private val _comments: MutableState<NetworkResult<List<CommentsWithUser>>> =
        mutableStateOf(NetworkResult.None())
    val comments: State<NetworkResult<List<CommentsWithUser>>> get() = _comments
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

    fun transform(data: List<CommentsWithUser>): List<Pair<User, Comment>> {
        val flatComments = mutableListOf<Pair<User, Comment>>()
        data.forEach { commentsWithUser ->
            if (commentsWithUser.user != null) {
                commentsWithUser.comments.forEach {
                    flatComments.add(commentsWithUser.user to it)
                }
            }
        }
        flatComments.sortBy { it.second.timestamp }
        return flatComments
    }

    private val _updateArticleMessageState: MutableState<DataState> = mutableStateOf(DataState.None)
    val updateArticleMessageState: DataState get() = _updateArticleMessageState.value
    fun copyLink() {
        val clipboard = LunimaryApplication.applicationContext
            .getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return
        ClipData.newPlainText(null, uiState.value.article.link).let {
            clipboard.setPrimaryClip(it)
        }
        updateArticleModifyState(DataState.Success("已复制:${uiState.value.article.link}"))
    }

    fun updateVisibility(mode: VisibleMode) {
        val article = uiState.value.article
        if (mode == article.visibleMode) {
            return
        }
        fly(FLY_UPDATE_ARTICLE_VISIBLE_MODE) {
            request(
                block = {
                    articleRepository.updateArticle(
                        article = article.copy(visibleMode = mode)
                    )
                },
                onSuccess = { _, _ ->
                    updateArticleModifyState(DataState.Success("已更新可见范围为：${mode.modeName}"))
                    val newArticle = article.copy(visibleMode = mode)
                    _uiState.value = uiState.value.copy(article = newArticle)
                },
                onFinish = { land(FLY_UPDATE_ARTICLE_VISIBLE_MODE) }
            )
        }
    }

    fun delete() {
        val article = uiState.value.article
        fly(FLY_DELETE_ARTICLE) {
            request(
                block = {
                    articleRepository.deleteArticleById(article.id)
                },
                onSuccess = { _, _ ->
                    updateArticleModifyState(DataState.Success("文章已删除"))
                    _uiState.value = uiState.value.copy(articleDeleted = true)
                },
                onFailed = {
                    updateArticleModifyState(DataState.Failed(Error(it)))
                },
                onFinish = { land(FLY_DELETE_ARTICLE) }
            )
        }
    }

    private fun updateArticleModifyState(
        newState: DataState
    ) {
        _updateArticleMessageState.value = newState
    }
}

data class UiState(
    val article: Article = Article.Default,
    val isFollowByMe: Boolean = false,
    /**
     * 是否已经查询到用户和文章所属用户的关系
     */
    val hasFetchedFriendship: Boolean = false,
    val articleDeleted: Boolean = false,
) {
    val isMyArticle: Boolean get() = article.userId == currentUser.id
}

const val FLY_FETCH_USER = "fly_fetch_user"
const val FLY_EXISTING_FRIENDSHIP = "fly_existing_friendship"
const val FLY_FOLLOW_OR_UNFOLLOW = "fly_follow_or_unfollow"
const val FLY_ABOUT_STAR = "____fly_about_star____"
const val FLY_ABOUT_LIKE = "____fly_about_like____"
const val FLY_CREATE_COMMENT = "fly_create_comment"
const val FLY_ALL_COMMENTS_OF_ARTICLE = "fly_all_comments_of_article"
const val FLY_UPDATE_ARTICLE_VISIBLE_MODE = "fly_update_article_visible_mode"
const val FLY_DELETE_ARTICLE = "fly_delete_article"