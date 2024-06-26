package com.example.lunimary.base

import com.example.lunimary.base.storage.TokenInfo
import com.example.lunimary.base.storage.lastLoginUser
import com.example.lunimary.base.storage.loadLocalToken
import com.example.lunimary.base.storage.loadSession
import com.example.lunimary.base.storage.removeLastLoginUser
import com.example.lunimary.base.storage.removeSession
import com.example.lunimary.base.storage.removeToken
import com.example.lunimary.base.storage.saveLastLoginUser
import com.example.lunimary.base.storage.saveSession
import com.example.lunimary.base.storage.saveTokens
import com.example.lunimary.model.User
import com.example.lunimary.util.logd
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UserState {
    private val userState: MutableStateFlow<User> =
        MutableStateFlow(lastLoginUser() ?: User.NONE)
    val currentUserState: StateFlow<User> get() = userState

    var updated: Boolean = false
        private set

    fun updateLocalUser(user: User, usernameChanged: Boolean = false) {
        if (usernameChanged) {
            updateSecurityUsername(newUsername = user.username)
        }
        "currentUser: $currentUser 新用户user:$user".logd()
        updated = true
        saveLastLoginUser(user)
        userState.value = user
    }

    fun clearUser() {
        "清除用户信息 currentUser: $currentUser".logd()
        userState.value = User.NONE
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

    private fun updateSecurityUsername(newUsername: String) {
        val session = loadSession()
        val localToken = loadLocalToken()
        removeToken()
        removeSession(currentUser.username)
        saveSession(username = newUsername, session = session)
        localToken?.let {
            saveTokens(
                tokenInfo = TokenInfo(
                    username = newUsername,
                    accessToken = localToken.accessToken,
                    refreshToken = localToken.refreshToken
                ),
                username = newUsername
            )
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