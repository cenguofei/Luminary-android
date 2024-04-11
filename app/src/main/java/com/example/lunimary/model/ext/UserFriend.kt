package com.example.lunimary.model.ext

import com.example.lunimary.model.User

@kotlinx.serialization.Serializable
data class UserFriend(
    val user: User,
    val beFriendTime: Long
)