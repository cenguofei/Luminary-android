package com.example.lunimary.ui.topicselect

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.lunimary.base.currentUser
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.viewmodel.BaseViewModel
import com.example.lunimary.base.viewmodel.parallelRequests
import com.example.lunimary.model.Topic
import com.example.lunimary.model.source.remote.repository.TopicRepository
import kotlinx.coroutines.CoroutineScope

class TopicSelectViewModel : BaseViewModel() {
    private val topicRepository = TopicRepository()

    private val _createOrUpdateState: MutableState<NetworkResult<Unit>> =
        mutableStateOf(NetworkResult.None())
    val createOrUpdateState: State<NetworkResult<Unit>> get() = _createOrUpdateState

    private val _uiState: MutableState<NetworkResult<UiState>> = mutableStateOf(NetworkResult.None())
    val uiState: NetworkResult<UiState> get() = _uiState.value

    private fun getData() {
        fly(FLY_GET_ALL_DATA) {
            _uiState.value = NetworkResult.Loading()
            parallelRequests(
                onSuccess = { userTopics, recommendTopics ->
                    _uiState.value = NetworkResult.Success(
                        data = UiState(
                            remoteSelectedTopics = userTopics,
                            recommendTopics = recommendTopics
                        )
                    )
                },
                onFailed = { _uiState.value = NetworkResult.Error(it) },
                onFinish = { land(FLY_GET_ALL_DATA) },
                block1 = { topicRepository.userTopics(currentUser.id) },
                block2 = { topicRepository.recommendTopics() }
            )
        }
    }

    fun createOrUpdate(coroutineScope: CoroutineScope) {
        if (uiState !is NetworkResult.Success) return
        val shouldUpdate = uiState.data?.shouldUpdate ?: return
        if (!shouldUpdate) return
        val newSelectedTopics = uiState.data?.selectedTopicsState?.map { it.id }
        if (newSelectedTopics.isNullOrEmpty()) return
        fly(FLY_CREATE_OR_UPDATE) {
            request(
                onSuccess = { _, _ ->
                    _createOrUpdateState.value = NetworkResult.Success()
                },
                onFailed = {
                    _createOrUpdateState.value = NetworkResult.Error(it)
                },
                onFinish = { land(FLY_CREATE_OR_UPDATE) },
                coroutineScope = coroutineScope
            ) {
                _createOrUpdateState.value = NetworkResult.Loading()
                topicRepository.createOrUpdate(currentUser.id, newSelectedTopics)
            }
        }
    }

    fun deleteSelectedTopic(topicId: Long) {
        if (uiState !is NetworkResult.Success) return
        uiState.data?.removeSelectedTopic(topicId)
    }

    fun addRecommendTopic(recommendTopicId: Long) {
        if (uiState !is NetworkResult.Success) return
        uiState.data?.addRecommendTopic(recommendTopicId)
    }

    init { getData() }
}

data class UiState(
    private val remoteSelectedTopics: List<Topic> = emptyList(),
    val recommendTopics: List<Topic> = emptyList()
) {
    private val _selectedTopicsState: MutableState<List<Topic>> = mutableStateOf(remoteSelectedTopics)
    val selectedTopicsState: List<Topic> get() = _selectedTopicsState.value

    fun removeSelectedTopic(topicId: Long) {
        _selectedTopicsState.value = selectedTopicsState.filter { it.id != topicId }
    }

    fun addRecommendTopic(recommendTopicId: Long) {
        val recommend = recommendTopics.find { it.id == recommendTopicId }
            ?: return
        if (recommendTopicId in selectedTopicsState.map { it.id }) return
        _selectedTopicsState.value = selectedTopicsState.toMutableList() + recommend.copy()
    }

    val shouldUpdate: Boolean get() {
        val remoteSelectedIds = remoteSelectedTopics.map { it.id }
        return remoteSelectedTopics.size != selectedTopicsState.size ||
                (selectedTopicsState.any { it.id !in remoteSelectedIds })
    }
}

private const val FLY_CREATE_OR_UPDATE = "fly_create_or_update"
private const val FLY_GET_ALL_DATA = "fly_get_all_data"