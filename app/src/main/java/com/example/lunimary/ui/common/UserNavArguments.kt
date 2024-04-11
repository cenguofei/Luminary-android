package com.example.lunimary.ui.common

import com.example.lunimary.base.LRUCache
import com.example.lunimary.model.User

object UserNavArguments : LRUCache<User>(capacity = 5)

const val VIEW_USER_KEY = "view_user_key"

fun setNavViewUser(user: User) {
    UserNavArguments[VIEW_USER_KEY] = user
}

fun getNavViewUser(): User {
    return UserNavArguments[VIEW_USER_KEY] ?: User.NONE
}