package com.example.lunimary.ui.user.information

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.request
import com.example.lunimary.models.UPLOAD_TYPE_USER_BACKGROUND
import com.example.lunimary.models.UPLOAD_TYPE_USER_HEAD
import com.example.lunimary.models.UploadData
import com.example.lunimary.models.source.remote.repository.FileRepository
import com.example.lunimary.network.NetworkResult

class InformationViewModel : BaseViewModel() {
    private val fileRepository = FileRepository()

    private val _showImageSelector: MutableState<Boolean> = mutableStateOf(false)
    val showImageSelector: State<Boolean> get() = _showImageSelector

    private val _uploadType: MutableState<UploadUserFileType> = mutableStateOf(UploadUserFileType.None)
    private val uploadType: State<UploadUserFileType> get() = _uploadType

    private val _headUploadState: MutableState<NetworkResult<UploadData>> = mutableStateOf(NetworkResult.None())
    val headUploadState: State<NetworkResult<UploadData>> get() = _headUploadState

    private val _backgroundUploadState: MutableState<NetworkResult<UploadData>> = mutableStateOf(NetworkResult.None())
    val backgroundUploadState: State<NetworkResult<UploadData>> get() = _backgroundUploadState

    fun updateShowImageSelector(show: Boolean, type: UploadUserFileType) {
        if (_uploadType.value != type) {
            _uploadType.value = type
        }
        _showImageSelector.value = show
    }

    fun clear() {
        _uploadType.value = UploadUserFileType.None
        _headUploadState.value = NetworkResult.None()
        _backgroundUploadState.value = NetworkResult.None()
    }

    fun uploadImage(
        path: String,
        filename: String
    ) {
        fly(FLY_UPLOAD_USER_HEAD_OR_BACKGROUND) {
            val outUploadState = when(uploadType.value) {
                UploadUserFileType.HeadImage -> _headUploadState
                else -> _backgroundUploadState
            }
            val uploadType = when(uploadType.value) {
                UploadUserFileType.HeadImage -> UPLOAD_TYPE_USER_HEAD
                else -> UPLOAD_TYPE_USER_BACKGROUND
            }
            request(
                block = {
                    outUploadState.value = NetworkResult.Loading()
                    fileRepository.uploadFile(path, filename, uploadType)
                },
                onSuccess = { data, msg ->
                    outUploadState.value = NetworkResult.Success(data = data, msg = msg)
                },
                onFailed = {
                    outUploadState.value = NetworkResult.Error(it)
                },
                onFinish = { land(FLY_UPLOAD_USER_HEAD_OR_BACKGROUND) }
            )
        }
    }
}

const val FLY_UPLOAD_USER_HEAD_OR_BACKGROUND = "fly_upload_user_head_or_background"

enum class UploadUserFileType {
    HeadImage,
    Background,
    None
}