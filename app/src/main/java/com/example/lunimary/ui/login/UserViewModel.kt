package com.example.lunimary.ui.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lunimary.base.BaseViewModel
import com.example.lunimary.base.request
import com.example.lunimary.models.responses.UserData
import com.example.lunimary.models.source.UserRepository
import com.example.lunimary.models.source.UserSource
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.storage.removeSession
import com.example.lunimary.storage.removeToken
import com.example.lunimary.util.UserState
import com.example.lunimary.util.logd
import com.example.lunimary.util.unknownErrorMsg
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel : BaseViewModel() {
    private val userSource: UserSource = UserRepository()

    private val _loginState: MutableLiveData<NetworkResult<UserData>> = MutableLiveData(NetworkResult.None())
    val loginState: LiveData<NetworkResult<UserData>> get() = _loginState

    private val _hasShowLogout: MutableState<Boolean> = mutableStateOf(false)
    val hasShowLogout: State<Boolean> get() = _hasShowLogout

    private val _logoutState: MutableLiveData<NetworkResult<Unit>> = MutableLiveData(NetworkResult.None())
    val logoutState: LiveData<NetworkResult<Unit>> get() = _logoutState

    fun login(
        username: String,
        password: String
    ) {
        request(
            block = {
                _loginState.postValue(NetworkResult.Loading())
                userSource.login(username, password)
            },
            onSuccess = {
                if (it?.user != null) {
                    _loginState.postValue(NetworkResult.Success(it))
                    UserState.updateUser(it.user)
                } else {
                    _loginState.postValue(NetworkResult.Error(unknownErrorMsg))
                }
            },
            onFailed = {
                _loginState.postValue(NetworkResult.Error(it))
            }
        )
    }

    fun getUser(id: Long) {
        request(
            block = { userSource.queryUser(id) },
            onSuccess = {
            }
        )
    }

    fun checkIsLogin() {
        request(
            block = {
                userSource.checkIsLogin()
            },
            onSuccess = {
                UserState.updateLoginState(it)
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
            onSuccess = {
                removeSession()
                removeToken()
                UserState.clearUser()
                _logoutState.postValue(NetworkResult.Success())
            },
            onFailed = {
                _logoutState.postValue(NetworkResult.Error(it))
            }
        )
    }

    fun register(username: String, password: String) {
        request(
            block = { userSource.register(username, password) },
            onSuccess = {

            }
        )
    }

    fun setHasShowLogout(hasShow: Boolean) {
        if (hasShowLogout.value != hasShow) {
            _hasShowLogout.value = hasShow
        }
    }
}
