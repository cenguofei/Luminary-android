package com.example.lunimary.ui.edit

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lunimary.base.currentUser
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.viewmodel.BaseViewModel
import com.example.lunimary.design.components.tagColors
import com.example.lunimary.model.Article
import com.example.lunimary.model.UploadData
import com.example.lunimary.model.VisibleMode
import com.example.lunimary.model.source.local.LocalArticleRepository
import com.example.lunimary.model.source.local.LocalTagRepository
import com.example.lunimary.model.source.local.Tag
import com.example.lunimary.model.source.remote.repository.AddArticleRepository
import com.example.lunimary.model.source.remote.repository.ArticleRepository
import com.example.lunimary.model.source.remote.repository.FileRepository
import com.example.lunimary.ui.browse.FLY_DELETE_ARTICLE
import com.example.lunimary.util.empty
import com.example.lunimary.util.logd
import com.example.lunimary.util.logi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditViewModel : BaseViewModel() {
    private val addArticleRepository by lazy { AddArticleRepository() }
    private val articleRepository by lazy { ArticleRepository() }
    private val tagRepository by lazy { LocalTagRepository() }
    private val fileRepository by lazy { FileRepository() }
    private val localArticleRepository by lazy { LocalArticleRepository() }

    private val _uploadCoverState: MutableState<NetworkResult<UploadData>> =
        mutableStateOf(NetworkResult.None())
    val uploadCoverState: State<NetworkResult<UploadData>> get() = _uploadCoverState

    private val _publishArticleState: MutableLiveData<NetworkResult<Unit>> =
        MutableLiveData(NetworkResult.None())
    val publishArticleState: LiveData<NetworkResult<Unit>> get() = _publishArticleState

    private val _updateArticleState: MutableStateFlow<NetworkResult<Unit>> =
        MutableStateFlow(NetworkResult.None())
    val updateArticleState: StateFlow<NetworkResult<Unit>> get() = _updateArticleState


    private val _deleteState: MutableStateFlow<NetworkResult<String>> =
        MutableStateFlow(NetworkResult.None())
    val deleteState: StateFlow<NetworkResult<String>> get() = _deleteState

    private val _uiState: MutableState<UiState> = mutableStateOf(UiState())
    val uiState: UiState get() = _uiState.value

    //////////////////////////// UI UnRelative ////////////////////////////////
    private var hasPublished = false
    fun publish() {
        if (hasPublished) return
        val timestamp = System.currentTimeMillis()
        val newArticle = uiState.generateArticle().copy(timestamp = timestamp)
        fly(FLY_ADD_ARTICLE) {
            request(
                block = {
                    _publishArticleState.postValue(NetworkResult.Loading())
                    addArticleRepository.addArticle(newArticle)
                },
                onSuccess = { _, _ ->
                    hasPublished = true
                    _publishArticleState.postValue(NetworkResult.Success())
                    clear()
                    if (uiState.editType == EditType.Draft) {
                        deleteDraft()
                    }
                },
                onFailed = {
                    _publishArticleState.postValue(NetworkResult.Error(it))
                },
                onFinish = {
                    land(FLY_ADD_ARTICLE)
                }
            )
        }
    }

    fun deleteDraft() {
        uiState.theArticle?.let {
            viewModelScope.launch {
                localArticleRepository.deleteArticle(it)
            }
        }
    }

    fun afterTitleChanged(title: String) {
        if (title != uiState.title) {
            _uiState.value = uiState.copy(title = title)
        }
    }

    fun afterBodyChanged(body: String) {
        if (body != uiState.body) {
            _uiState.value = uiState.copy(
                body = body,
            )
        }
    }

    private var lastSaveDraft: Article? = null
    val shouldSaveAsDraft: Boolean get() = lastSaveDraft == null || uiState.generateArticle() != lastSaveDraft
    var saveAsDraftEnabled = true
    fun saveAsDraft() {
        if (!saveAsDraftEnabled) return
        val saveArticle = uiState.generateArticle()
        "lastSaveDraft != saveArticle = ${lastSaveDraft != saveArticle}".logd("update_test")
        if (lastSaveDraft == null || lastSaveDraft != saveArticle) {
            lastSaveDraft = saveArticle
            viewModelScope.launch {
                localArticleRepository.insertArticle(saveArticle)
            }
        } else {
            "saveAsDraft: 已经更新过".logd("update_test")
        }
    }

    private var lastUpdateDraft: Article? = null
    val shouldUpdateDraft: Boolean get() = lastUpdateDraft == null || uiState.updatedLocalArticle() != lastUpdateDraft
    fun updateDraft() {
        val saveArticle = uiState.updatedLocalArticle()
        "lastUpdateDraft != saveArticle = ${lastUpdateDraft != saveArticle}".logd("update_test")
        if (lastUpdateDraft == null || lastUpdateDraft != saveArticle) {
            viewModelScope.launch {
                saveArticle?.let {
                    lastUpdateDraft = saveArticle
                    localArticleRepository.update(it)
                }
            }
        } else {
            "updateDraft: 已经更新过".logd("update_test")
        }
    }

    private var lastUpdateRemoteArticle: Article? = null
    fun updateRemoteArticle(onUpdateSuccess: (updated: Article) -> Unit) {
        val generateArticle = uiState.generateArticle()
        if (lastUpdateRemoteArticle == null || lastUpdateRemoteArticle != generateArticle) {
            lastUpdateRemoteArticle = generateArticle
            fly(FLY_UPLOAD_REMOTE_ARTICLE) {
                request(
                    block = {
                        _updateArticleState.value = NetworkResult.Loading()
                        articleRepository.updateArticle(generateArticle)
                    },
                    onFailed = {
                        _updateArticleState.value = NetworkResult.Error(it)
                    },
                    onSuccess = { _, _ ->
                        onUpdateSuccess(generateArticle)
                        _updateArticleState.value = NetworkResult.Success()
                    },
                    onFinish = { land(FLY_UPLOAD_REMOTE_ARTICLE) }
                )
            }
        } else {
            "updateRemoteArticle: 已经更新过".logd("update_test")
        }
    }

    fun addNewTag(tag: String) {
        val tags = uiState.tags
        if (tag.isBlank() || tag in tags.map { it.name }) {
            return
        }
        val newTag = Tag(
            name = tag,
            username = currentUser.username,
            color = tagColors.random().toArgb()
        )
        _uiState.value = uiState.copy(
            tags = tags + newTag,
        )
        viewModelScope.launch {
            tagRepository.createTag(newTag)
        }
    }

    fun addHistoryTagToArticle(tag: Tag) {
        val oldTags = uiState.tags
        if (tag in oldTags
            || tag.id in oldTags.map { it.id }
            || tag.name in oldTags.map { it.name }
        ) {
            return
        }
        _uiState.value = uiState.copy(
            tags = oldTags + tag,
        )
    }

    fun removeHistoryTag(removeTag: Tag) {
        viewModelScope.launch {
            tagRepository.deleteTag(removeTag)
        }
    }

    fun visibleModeChange(mode: VisibleMode) {
        _uiState.value = uiState.copy(visibleMode = mode)
    }

    fun fillArticle(
        theArticle: Article?,
        editType: EditType
    ) {
        if (theArticle == null) return
        _uiState.value = uiState.copy(
            theArticle = theArticle,
            editType = editType,
            title = theArticle.title,
            body = theArticle.body,
            visibleMode = theArticle.visibleMode,
            cover = theArticle.cover,
            isFillByArticle = true
        )
        val tags = getHistoryTags().value
        "tags=$tags".logi("fill_draft_article") //will print null
    }

    private var hasUpdate = false
    fun updateTagsAfterGetHistoryTags(liveData: List<Tag>?) {
        if (hasUpdate) return
        hasUpdate = true
        val theArticle = uiState.theArticle
        liveData ?: return; theArticle ?: return
        val existTags = liveData.filter { it.name in theArticle.tags }
        val existTagsName = liveData.map { it.name }
        val absentTags = theArticle.tags.filter { it !in existTagsName }
            .map {
                Tag(
                    name = it,
                    color = tagColors.random().toArgb(),
                    username = currentUser.username
                )
            }
        _uiState.value = uiState.copy(
            tags = existTags + absentTags,
        )
    }

    fun getHistoryTags(): LiveData<List<Tag>> {
        return tagRepository.getHistoryTags(currentUser.username)
    }

    fun updateUri(uri: Uri) {
        _uiState.value = uiState.copy(
            uri = uri,
        )
    }

    fun uploadFile(path: String, filename: String) {
        fly(FLY_UPLOAD_FILE) {
            request(
                block = {
                    _uploadCoverState.value = NetworkResult.Loading()
                    fileRepository.uploadFile(path, filename)
                },
                onSuccess = { data, msg ->
                    _uiState.value = uiState.copy(
                        cover = data?.first() ?: empty,
                    )
                    _uploadCoverState.value = NetworkResult.Success(data = data, msg = msg)
                },
                onFailed = {
                    _uploadCoverState.value = NetworkResult.Error(it)
                },
                onFinish = { land(FLY_UPLOAD_FILE) }
            )
        }
    }

    fun removeAddedTag(tag: Tag) {
        val oldTags = uiState.tags
        _uiState.value = uiState.copy(
            tags = oldTags.filter { it.id != tag.id }
        )
    }

    fun deletePublishedArticle() {
        val article = uiState.theArticle ?: return
        fly(FLY_DELETE_ARTICLE) {
            request(
                block = {
                    _deleteState.value = NetworkResult.Loading()
                    articleRepository.deleteArticleById(article.id)
                },
                onSuccess = { _, msg ->
                    _deleteState.value = NetworkResult.Success(msg = msg)
                },
                onFailed = {
                    _deleteState.value = NetworkResult.Error(it)
                },
                onFinish = { land(FLY_DELETE_ARTICLE) }
            )
        }
    }

    private fun clear() {
        _uiState.value = uiState.copy(
            title = empty,
            body = empty,
            cover = empty,
            tags = emptyList(),
            visibleMode = VisibleMode.PUBLIC,
        )
    }
}

const val FLY_ADD_ARTICLE = "fly_add_article"
const val FLY_UPLOAD_FILE = "fly_upload_file"
const val FLY_UPLOAD_REMOTE_ARTICLE = "fly_update_remote_article"