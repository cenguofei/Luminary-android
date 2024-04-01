package com.example.lunimary.ui.privacy

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.network.NetworkResult

const val FLY_GET_PRIVACY = "fly_get_privacy"

class PrivacyViewModel : BaseViewModel() {
    private val repository: PrivacySource = PrivacySourceImpl()

    private val _privacy: MutableState<NetworkResult<String>> = mutableStateOf(NetworkResult.None())
    val privacy: State<NetworkResult<String>> get() = _privacy

    init { getPrivacy() }

    private fun getPrivacy() {
        fly(FLY_GET_PRIVACY) {
            request(
                block = {
                    _privacy.value = NetworkResult.Loading()
                    repository.getPrivacy()
                },
                onSuccess = { data, _ ->
                    _privacy.value = NetworkResult.Success(data)
                },
                onFailed = {
                    _privacy.value = NetworkResult.Error(it)
                },
                onFinish = { land(FLY_GET_PRIVACY) }
            )
        }
    }
}