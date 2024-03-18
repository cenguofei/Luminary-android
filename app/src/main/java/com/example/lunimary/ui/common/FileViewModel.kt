package com.example.lunimary.ui.common

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.request
import com.example.lunimary.models.UploadData
import com.example.lunimary.models.source.remote.repository.FileRepository
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.ui.edit.FLY_UPLOAD_FILE

class FileViewModel : BaseViewModel() {
    private val fileRepository = FileRepository()

    private val _uri = mutableStateOf(Uri.EMPTY)
    val uri: State<Uri> get() = _uri

    private val _uploadSuccess: MutableState<Boolean> = mutableStateOf(false)
    val uploadSuccess: State<Boolean> get() = _uploadSuccess

    private val _showImageSelector: MutableState<Boolean> = mutableStateOf(false)
    val showImageSelector: State<Boolean> get() = _showImageSelector

    fun updateSelectedFile(uri: Uri, path: String, filename: String) {
        _uri.value = uri
        _uploadSuccess.value = false
        uploadImage(path, filename)
    }

    private val _uploadState: MutableState<NetworkResult<UploadData>> = mutableStateOf(NetworkResult.None())
    val uploadState: State<NetworkResult<UploadData>> get() = _uploadState

    private fun uploadImage(path: String, filename: String) {
        fly(FLY_UPLOAD_FILE) {
            request(
                block = {
                    _uploadState.value = NetworkResult.Loading()
                    fileRepository.uploadFile(path, filename)
                },
                onSuccess = { data, msg ->
                    _uploadSuccess.value = true
                    _uploadState.value = NetworkResult.Success(data = data, msg = msg)
                },
                onFailed = {
                    _uploadState.value = NetworkResult.Error(it)
                },
                onFinish = { land(FLY_UPLOAD_FILE) }
            )
        }
    }

    fun updateShowImageSelector(show: Boolean) {
        _showImageSelector.value = show
    }

    fun clear() {
        _uri.value = Uri.EMPTY
        _uploadSuccess.value = false
        _showImageSelector.value = false
        _uploadState.value = NetworkResult.None()
    }
}