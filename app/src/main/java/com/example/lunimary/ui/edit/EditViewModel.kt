package com.example.lunimary.ui.edit

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.request
import com.example.lunimary.design.tagColors
import com.example.lunimary.models.Article
import com.example.lunimary.models.UploadData
import com.example.lunimary.models.VisibleMode
import com.example.lunimary.models.source.local.LocalArticleRepository
import com.example.lunimary.models.source.local.LocalTagRepository
import com.example.lunimary.models.source.local.Tag
import com.example.lunimary.models.source.remote.repository.AddArticleRepository
import com.example.lunimary.models.source.remote.repository.FileRepository
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.util.currentUser
import com.example.lunimary.util.empty
import com.example.lunimary.util.logd
import kotlinx.coroutines.launch

class EditViewModel : BaseViewModel() {
    private val addArticleRepository = AddArticleRepository()
    private val tagRepository = LocalTagRepository()
    private val fileRepository = FileRepository()

    private val _articleDataState: MutableState<ArticleData> = mutableStateOf(ArticleData())
    val articleDataState: State<ArticleData> get() = _articleDataState

    var title: String = empty
        set(value) {
            if (field != value) {
                field = value
            }
        }

    var body: String = empty
        set(value) {
            if (field != value) {
                field = value
            }
        }

    private val _tags: MutableState<List<Tag>> = mutableStateOf(emptyList())
    val tags: State<List<Tag>> get() = _tags

    private var _visibleMode: MutableState<VisibleMode> = mutableStateOf(VisibleMode.PUBLIC)
    val visibleMode: State<VisibleMode> get() = _visibleMode

    private var _cover: MutableState<String> = mutableStateOf(empty)
    val cover: State<String> get() = _cover

    private fun clear() {
        title = empty
        body = empty
        _cover.value = empty
        _tags.value = emptyList()
        _visibleMode.value = VisibleMode.PUBLIC
    }

    private val _addArticleState: MutableLiveData<NetworkResult<Unit>> =
        MutableLiveData(NetworkResult.None())
    val addArticleState: LiveData<NetworkResult<Unit>> get() = _addArticleState

    private var hasPublished = false
    fun publish() {
        if (hasPublished) return
        val newArticle = generateArticle()
        fly(FLY_ADD_ARTICLE) {
            request(
                block = {
                    _addArticleState.postValue(NetworkResult.Loading())
                    addArticleRepository.addArticle(newArticle)
                },
                onSuccess = { _, _ ->
                    hasPublished = true
                    _addArticleState.postValue(NetworkResult.Success())
                    clear()
                },
                onFailed = {
                    _addArticleState.postValue(NetworkResult.Error(it))
                },
                onFinish = {
                    land(FLY_ADD_ARTICLE)
                }
            )
        }
    }

    fun canPublish(): Boolean = title.isNotBlank() && body.isNotBlank()

    fun anyNotEmpty(): Boolean {
        return title.isNotBlank() || body.isNotBlank() || tags.value.isNotEmpty()
                || cover.value.isNotBlank()
    }

    fun checkArticleParams(
        success: () -> Unit,
        problem: (String) -> Unit
    ) {
        if (tags.value.isEmpty()) {
            problem("请添加标签！")
        } else if (false) {

        } else {
            success()
        }
    }

    private val articleRepository = LocalArticleRepository()
    fun saveAsDraft() {
        val saveArticle = generateArticle()
        viewModelScope.launch {
            articleRepository.insertArticle(saveArticle)
        }
    }


    fun updateDraft() {
        if (isFillByArticle) {
            viewModelScope.launch {
                filledArticle?.let { fill ->
                    articleRepository.update(fill.copy(
                        title = title,
                        body = body,
                        tags = tags.value.map { it.name }.toTypedArray(),
                        visibleMode = visibleMode.value,
                        cover = cover.value
                    ))
                }
            }
        }
    }

    fun draftChanged(): Boolean = (filledArticle?.let {
        it.title != title || it.body != body
                || tags.value.size != it.tags.size
                || !tags.value.all { t -> t.name in it.tags }
                || visibleMode.value != it.visibleMode
                || cover.value != it.cover
    } ?: false).also {
        "draftChanged:$it, tags=${tags.value}, fillTags=${filledArticle?.tags.toString()}"
            .logd("live_test")
    }

    fun addNewTag(tag: String) {
        if (tag.isBlank() || tag in tags.value.map { it.name }) {
            return
        }
        val newTag =
            Tag(name = tag, username = currentUser.username, color = tagColors.random().toArgb())
        _tags.value = tags.value + newTag
        viewModelScope.launch {
            tagRepository.createTag(newTag)
        }
    }

    fun addHistoryTagToArticle(tag: Tag) {
        _tags.value = tags.value + tag
    }

    fun removeHistoryTag(removeTag: Tag) {
        viewModelScope.launch {
            tagRepository.deleteTag(removeTag)
        }
    }

    fun visibleModeChange(mode: VisibleMode) {
        _visibleMode.value = mode
    }

    var isFillByArticle = false
        private set
    private var filledArticle: Article? = null
    private var hasFilled = false
    fun fillDraftArticle(draftArticle: Article?) {
        if (draftArticle == null || hasFilled) return
        hasFilled = true
        isFillByArticle = true
        filledArticle = draftArticle
        "fillDraftArticle=$filledArticle".logd("live_test")
        title = draftArticle.title
        body = draftArticle.body
        _visibleMode.value = draftArticle.visibleMode
        _cover.value = draftArticle.cover
        _articleDataState.value = ArticleData(title = draftArticle.title, body = draftArticle.body)
    }

    private fun generateArticle(): Article = Article(
        title = title,
        body = body,
        userId = currentUser.id,
        username = currentUser.username,
        author = currentUser.username,
        timestamp = System.currentTimeMillis(),
        visibleMode = visibleMode.value,
        tags = tags.value.map { it.name }.toTypedArray(),
        cover = cover.value
    )

    fun getHistoryTags(): LiveData<List<Tag>> {
        return tagRepository.getHistoryTags(currentUser.username)
    }

    private var hasUpdate = false
    fun updateTagsAfterGetHistoryTags(liveData: List<Tag>?) {
        if (hasUpdate) return
        hasUpdate = true
        if (liveData != null) {
            if (filledArticle != null) {
                val existTags = liveData.filter { it.name in filledArticle!!.tags }
                val existTagsName = liveData.map { it.name }
                val absentTags = filledArticle!!.tags.filter { it !in existTagsName }
                    .map {
                        Tag(
                            name = it,
                            color = tagColors.random().toArgb(),
                            username = currentUser.username
                        )
                    }
                _tags.value = existTags + absentTags
                "tags.value:${_tags.value}".logd("live_test")
            }
        }
    }

    private val _uri = mutableStateOf(Uri.EMPTY)
    val uri: State<Uri> get() = _uri

    fun updateUri(uri: Uri) {
        _uri.value = uri
    }

    private val _uploadCoverState: MutableState<NetworkResult<UploadData>> = mutableStateOf(NetworkResult.None())
    val uploadCoverState: State<NetworkResult<UploadData>> get() = _uploadCoverState
    fun uploadFile(path: String, filename: String) {
        fly(FLY_UPLOAD_FILE) {
            request(
                block = {
                    _uploadCoverState.value = NetworkResult.Loading()
                    fileRepository.uploadFile(path, filename)
                },
                onSuccess = { data, msg ->
                    _cover.value = data?.first() ?: empty
                    _uploadCoverState.value = NetworkResult.Success(data = data, msg = msg)
                },
                onFailed = {
                    _uploadCoverState.value = NetworkResult.Error(it)
                },
                onFinish = { land(FLY_UPLOAD_FILE) }
            )
        }
    }

    init {
        getHistoryTags()
    }
}

const val FLY_ADD_ARTICLE = "fly_add_article"
const val FLY_UPLOAD_FILE = "fly_upload_file"