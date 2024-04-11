package com.example.lunimary.ui.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.model.UploadData
import com.example.lunimary.model.source.remote.repository.FileRepository
import com.example.lunimary.ui.edit.FLY_UPLOAD_FILE

class FileViewModel : BaseViewModel() {
    private val fileRepository = FileRepository()

    private val _showImageSelector: MutableState<Boolean> = mutableStateOf(false)
    val showImageSelector: State<Boolean> get() = _showImageSelector

    fun updateSelectedFile(
        path: String,
        filename: String,
        outUploadState: MutableState<NetworkResult<UploadData>>? = null
    ) {
        uploadImage(path, filename, outUploadState)
    }

    private val _uploadState: MutableState<NetworkResult<UploadData>> = mutableStateOf(NetworkResult.None())
    val uploadState: State<NetworkResult<UploadData>> get() = _uploadState

    private fun uploadImage(
        path: String,
        filename: String,
        outUploadState: MutableState<NetworkResult<UploadData>>?
    ) {
        fly(FLY_UPLOAD_FILE) {
            request(
                block = {
                    (outUploadState ?: _uploadState).value = NetworkResult.Loading()
                    fileRepository.uploadFile(path, filename)
                },
                onSuccess = { data, msg ->
                    (outUploadState ?: _uploadState).value = NetworkResult.Success(data = data, msg = msg)
                },
                onFailed = {
                    (outUploadState ?: _uploadState).value = NetworkResult.Error(it)
                },
                onFinish = { land(FLY_UPLOAD_FILE) }
            )
        }
    }

    fun updateShowImageSelector(show: Boolean) {
        _showImageSelector.value = show
    }

    fun clear() {
        _showImageSelector.value = false
        _uploadState.value = NetworkResult.None()
    }
}