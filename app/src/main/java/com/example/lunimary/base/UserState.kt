package com.example.lunimary.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lunimary.base.storage.lastLoginUser
import com.example.lunimary.base.storage.removeLastLoginUser
import com.example.lunimary.base.storage.saveLastLoginUser
import com.example.lunimary.model.User
import com.example.lunimary.util.logd
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UserState {
    private val CURRENT_USER: MutableStateFlow<User> = MutableStateFlow(lastLoginUser() ?: User.NONE)
    val currentUserState: StateFlow<User> get() = CURRENT_USER

    var updated: Boolean = false
        private set

    fun updateLocalUser(user: User) {
        "currentUser: $currentUser".logd()
        "更新用户user:$user".logd()
        updated = true
        saveLastLoginUser(user)
        CURRENT_USER.value = user
    }

    fun clearUser() {
        "清除用户信息 currentUser: $currentUser".logd()
        CURRENT_USER.value = User.NONE
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

val currentUser: User get() = UserState.currentUserState.value

fun notLogin(): Boolean = currentUser == User.NONE

fun checkLogin(
    isLogin: () -> Unit = {},
    isLogout: () -> Unit = {}
) {
    if (notLogin()) isLogout() else isLogin()
}

@Composable
fun UserStateEffect(
    key: String,
    whenNoLogin: () -> Unit = {},
    onCollected: (User) -> Unit = {}
) {
    val userState = UserState.currentUserState.collectAsStateWithLifecycle()
    LaunchedEffect(
        key1 = userState.value,
        block = {
            "UserStateEffect $key collected userState:${userState.value}".logd("currentUserState")
            onCollected(userState.value)
            if (userState.value == User.NONE) {
                whenNoLogin()
            }
        }
    )
}
