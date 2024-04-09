package com.example.lunimary.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lunimary.base.storage.lastLoginUser
import com.example.lunimary.base.storage.removeLastLoginUser
import com.example.lunimary.base.storage.saveLastLoginUser
import com.example.lunimary.models.User
import com.example.lunimary.util.logd

object UserState {
    private val CURRENT_USER: MutableLiveData<User> = MutableLiveData(lastLoginUser() ?: User.NONE)
    val currentUserState: LiveData<User> get() = CURRENT_USER

    var updated: Boolean = false
        private set

    fun updateLocalUser(user: User) {
        "currentUser: $currentUser".logd()
        "更新用户user:$user".logd()
        updated = true
        saveLastLoginUser(user)
        CURRENT_USER.postValue(user)
    }

    fun clearUser() {
        "清除用户信息 currentUser: $currentUser".logd()
        CURRENT_USER.postValue(User.NONE)
        removeLastLoginUser()
        updated = true
    }

    fun updateLoginState(isLogin: Boolean?, from: String) {
        "currentUser: $currentUser".logd()
        "更新登录状态,from=$from isLogin:$isLogin".logd()
        if (isLogin != null) {
            if (!isLogin) {
                //TODO 测试阶段暂时步清空用户，上线后需要改为清空
                //clearUser()
            }
        }
    }
}

val currentUser: User get() = UserState.currentUserState.value ?: User.NONE

fun notLogin(): Boolean = currentUser == User.NONE

fun checkLogin(
    isLogin: () -> Unit = {},
    isLogout: () -> Unit = {}
) {
    if (notLogin()) isLogout() else isLogin()
}
