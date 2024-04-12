package com.example.lunimary.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lunimary.LunimaryApplication
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.UserState
import com.example.lunimary.base.mmkv.SettingMMKV
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.network.isCurrentlyConnected
import com.example.lunimary.base.storage.refreshToken
import com.example.lunimary.base.storage.removeSession
import com.example.lunimary.base.storage.removeToken
import com.example.lunimary.model.responses.UserData
import com.example.lunimary.model.source.remote.repository.UserRepository
import com.example.lunimary.util.logd
import com.example.lunimary.util.unknownErrorMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserViewModel : BaseViewModel() {
    private val userSource = UserRepository()

    private val _loginState: MutableLiveData<NetworkResult<UserData>> =
        MutableLiveData(NetworkResult.None())
    val loginState: LiveData<NetworkResult<UserData>> get() = _loginState

    private val _logoutState: MutableLiveData<NetworkResult<Unit>> =
        MutableLiveData(NetworkResult.None())
    val logoutState: LiveData<NetworkResult<Unit>> get() = _logoutState


    fun checkLogin(
        isLogin: () -> Unit = {},
        logout: () -> Unit = {}
    ) {
        if (SettingMMKV.hasLogout) {
            return
        }
        if (!LunimaryApplication.applicationContext.isCurrentlyConnected()) {
            return
        }
        runCatching {
            viewModelScope.launch(Dispatchers.IO) {
                val deferred = async { userSource.checkIsLogin() }
                val response = deferred.await()
                "检查登录状态：response=${response.data.toString() + response.code + response.msg}".logd()
                if (response.data?.isLogin == true) {
                    isLogin()
                } else {
                    val deffer = async { refreshToken() }
                    val tokens = deffer.await()
                    if (tokens == null) {
                        logout()
                    } else {
                        isLogin()
                    }
                }
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    fun login(
        username: String,
        password: String
    ) {
        request(
            block = {
                _loginState.postValue(NetworkResult.Loading())
                userSource.login(username, password)
            },
            onSuccess = { data, _ ->
                if (data?.user != null) {
                    _loginState.postValue(NetworkResult.Success(data))
                    SettingMMKV.hasLogout = false
                    UserState.updateLocalUser(data.user)
                } else {
                    _loginState.postValue(NetworkResult.Error(unknownErrorMsg))
                }
            },
            onFailed = {
                _loginState.postValue(NetworkResult.Error(it))
            }
        )
    }

    fun logout() {
        request(
            block = {
                _loginState.postValue(NetworkResult.None())
                _logoutState.postValue(NetworkResult.Loading())
                userSource.logout()
            },
            onSuccess = { _, _ ->
                removeSession()
                removeToken()
                UserState.clearUser()
                SettingMMKV.hasLogout = true
                _logoutState.postValue(NetworkResult.Success())
            },
            onFailed = {
                _logoutState.postValue(NetworkResult.Error(it))
            }
        )
    }

    private val _registerState: MutableLiveData<NetworkResult<UserData>> =
        MutableLiveData(NetworkResult.None())
    val registerState: LiveData<NetworkResult<UserData>> get() = _registerState

    fun register(username: String, password: String) {
        request(
            block = {
                _registerState.postValue(NetworkResult.Loading())
                userSource.register(username, password)
            },
            onSuccess = { data, msg ->
                data?.let { userData ->
                    _registerState.postValue(NetworkResult.Success(userData, msg))
                } ?: _registerState.postValue(NetworkResult.Error(msg ?: unknownErrorMsg))
            },
            onFailed = {
                _registerState.postValue(NetworkResult.Error(it))
            }
        )
    }

    fun resetRegisterState() {
        _registerState.postValue(NetworkResult.None())
    }

    fun reset() {
        _logoutState.postValue(NetworkResult.None())
    }
}
