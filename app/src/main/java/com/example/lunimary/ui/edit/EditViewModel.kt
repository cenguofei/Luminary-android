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
import com.example.lunimary.base.currentUser
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.design.tagColors
import com.example.lunimary.models.Article
import com.example.lunimary.models.UploadData
import com.example.lunimary.models.VisibleMode
import com.example.lunimary.models.source.local.LocalArticleRepository
import com.example.lunimary.models.source.local.LocalTagRepository
import com.example.lunimary.models.source.local.Tag
import com.example.lunimary.models.source.remote.repository.AddArticleRepository
import com.example.lunimary.models.source.remote.repository.ArticleRepository
import com.example.lunimary.models.source.remote.repository.FileRepository
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

    private val _uiState: MutableState<UiState> = mutableStateOf(UiState())
    val uiState: State<UiState> get() = _uiState

    //////////////////////////// UI UnRelative ////////////////////////////////
    private var hasPublished = false
    fun publish(theArticle: Article?) {
        if (hasPublished) return
        val timestamp = System.currentTimeMillis()
        val newArticle = uiState.value.generateArticle().copy(timestamp = timestamp)
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
                    if (uiState.value.editType == EditType.Draft) {
                        theArticle?.let {
                            viewModelScope.launch {
                                localArticleRepository.deleteArticle(it)
                            }
                        }
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

    fun afterTitleChanged(title: String) {
        if (title != uiState.value.title) {
            _uiState.value = uiState.value.copy(title = title)
        }
    }

    fun afterBodyChanged(body: String) {
        if (body != uiState.value.body) {
            _uiState.value = uiState.value.copy(
                body = body,
            )
        }
    }

    private var lastSaveDraft: Article? = null
    val shouldSaveAsDraft: Boolean get() = lastSaveDraft == null || uiState.value.generateArticle() != lastSaveDraft
    fun saveAsDraft() {
        val saveArticle = uiState.value.generateArticle()
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
    val shouldUpdateDraft: Boolean get() = lastUpdateDraft == null || uiState.value.updatedLocalArticle() != lastUpdateDraft
    fun updateDraft() {
        val saveArticle = uiState.value.updatedLocalArticle()
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
        val generateArticle = uiState.value.generateArticle()
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
        val tags = uiState.value.tags
        if (tag.isBlank() || tag in tags.map { it.name }) {
            return
        }
        val newTag = Tag(
            name = tag,
            username = currentUser.username,
            color = tagColors.random().toArgb()
        )
        _uiState.value = uiState.value.copy(
            tags = tags + newTag,
        )
        viewModelScope.launch {
            tagRepository.createTag(newTag)
        }
    }

    fun addHistoryTagToArticle(tag: Tag) {
        val oldTags = uiState.value.tags
        if (tag in oldTags
            || tag.id in oldTags.map { it.id }
            || tag.name in oldTags.map { it.name }) {
            return
        }
        _uiState.value = uiState.value.copy(
            tags = oldTags + tag,
        )
    }

    fun removeHistoryTag(removeTag: Tag) {
        viewModelScope.launch {
            tagRepository.deleteTag(removeTag)
        }
    }

    fun visibleModeChange(mode: VisibleMode) {
        _uiState.value = uiState.value.copy(visibleMode = mode)
    }

    fun fillArticle(
        theArticle: Article?,
        editType: EditType
    ) {
        if (theArticle == null) return
        _uiState.value = uiState.value.copy(
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
        val theArticle = uiState.value.theArticle
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
        _uiState.value = uiState.value.copy(
            tags = existTags + absentTags,
        )
    }

    fun getHistoryTags(): LiveData<List<Tag>> {
        return tagRepository.getHistoryTags(currentUser.username)
    }

    fun updateUri(uri: Uri) {
        _uiState.value = uiState.value.copy(
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
                    _uiState.value = uiState.value.copy(
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
        val oldTags = uiState.value.tags
        _uiState.value = uiState.value.copy(
            tags = oldTags.filter { it.id != tag.id }
        )
    }

    private fun clear() {
        _uiState.value = uiState.value.copy(
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