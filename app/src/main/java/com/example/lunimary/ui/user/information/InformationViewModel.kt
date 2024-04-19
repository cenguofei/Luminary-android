package com.example.lunimary.ui.user.information

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.lunimary.base.UserState
import com.example.lunimary.base.currentUser
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.viewmodel.BaseViewModel
import com.example.lunimary.model.UPLOAD_TYPE_USER_BACKGROUND
import com.example.lunimary.model.UPLOAD_TYPE_USER_HEAD
import com.example.lunimary.model.UploadData
import com.example.lunimary.model.User
import com.example.lunimary.model.source.remote.repository.FileRepository
import com.example.lunimary.model.source.remote.repository.UserRepository

class InformationViewModel : BaseViewModel() {
    private val fileRepository = FileRepository()
    private val userRepository = UserRepository()

    private val _showImageSelector: MutableState<Boolean> = mutableStateOf(false)
    val showImageSelector: State<Boolean> get() = _showImageSelector

    private val _uploadType: MutableState<UploadUserFileType> = mutableStateOf(UploadUserFileType.None)
    private val uploadType: State<UploadUserFileType> get() = _uploadType

    private val _headUploadState: MutableState<NetworkResult<UploadData>> = mutableStateOf(
        NetworkResult.None())
    val headUploadState: State<NetworkResult<UploadData>> get() = _headUploadState

    private val _backgroundUploadState: MutableState<NetworkResult<UploadData>> = mutableStateOf(
        NetworkResult.None())
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

    private val _updateUserState: MutableState<NetworkResult<Unit>> = mutableStateOf(NetworkResult.None())
    val updateUserState: State<NetworkResult<Unit>> get() = _updateUserState

    fun save(newUser: User) {
        if (newUser == currentUser) return
        fly(FLY_UPDATE_USER) {
            request(
                block = {
                    _updateUserState.value = NetworkResult.Loading()
                    userRepository.update(newUser)
                },
                onSuccess = { _, _ ->
                    UserState.updateLocalUser(
                        user = newUser.copy(),
                        usernameChanged = currentUser.username != newUser.username
                    )
                    _updateUserState.value = NetworkResult.Success()
                },
                onFailed = {
                    _updateUserState.value = NetworkResult.Error(it)
                },
                onFinish = { land(FLY_UPDATE_USER) }
            )
        }
    }
}

const val FLY_UPLOAD_USER_HEAD_OR_BACKGROUND = "fly_upload_user_head_or_background"
const val FLY_UPDATE_USER = "fly_update_user"

enum class UploadUserFileType {
    HeadImage,
    Background,
    None
}