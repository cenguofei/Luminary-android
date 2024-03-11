package com.example.lunimary.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lunimary.models.User
import com.example.lunimary.storage.lastLoginUser
import com.example.lunimary.storage.removeLastLoginUser
import com.example.lunimary.storage.saveLastLoginUser

object UserState {
    private val CURRENT_USER: MutableLiveData<User> = MutableLiveData(lastLoginUser() ?: User.NONE)
    val currentUserState: LiveData<User> get() = CURRENT_USER

    private val IS_LOGIN: MutableLiveData<Boolean> = MutableLiveData(CURRENT_USER.value != User.NONE)
    val isLoginState: LiveData<Boolean> get() = IS_LOGIN

    var updated: Boolean = false
        private set

    var message: String? = null
        set(value) {
            if (value != null) {
                field = value
            }
        }

    fun updateUser(user: User) {
        "currentUser: $currentUser".logd()
        "更新用户:user:$user".logd()
        updated = true
        saveLastLoginUser(user)
        CURRENT_USER.postValue(user)
    }

    fun clearUser() {
        "currentUser: $currentUser".logd()
        "清除用户信息".logd()
        CURRENT_USER.postValue(User.NONE)
        removeLastLoginUser()
        updated = true
    }

    fun updateLoginState(isLogin: Boolean?, from: String) {
        "currentUser: $currentUser".logd()
        "更新登录状态,from=$from isLogin:$isLogin".logd()
        if (isLogin != null) {
            if (!isLogin) {
                clearUser()
            }
        }
    }
}

val currentUser: User get() = UserState.currentUserState.value ?: User.NONE

val isLogin: Boolean get() = UserState.isLoginState.value ?: false

fun notLogin(): Boolean = currentUser == User.NONE

fun checkLogin(
    isLogin: () -> Unit = {},
    isLogout: () -> Unit = {}
) {
    if (notLogin()) {
        isLogout()
    } else {
        isLogin()
    }
}
