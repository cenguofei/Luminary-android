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

    fun updateUser(user: User) {
        saveLastLoginUser(user)
        CURRENT_USER.postValue(user)
    }

    fun clearUser() {
        CURRENT_USER.value = User.NONE
        removeLastLoginUser()
    }

    fun updateLoginState(isLogin: Boolean?) {
        if (isLogin != null) {
            if (!isLogin) {
                clearUser()
            }
        }
    }
}

val currentUser: User get() = UserState.currentUserState.value ?: User.NONE

val isLogin: Boolean get() = UserState.isLoginState.value ?: false
