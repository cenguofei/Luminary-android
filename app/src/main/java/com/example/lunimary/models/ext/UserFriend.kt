package com.example.lunimary.models.ext

import com.example.lunimary.models.User

@kotlinx.serialization.Serializable
data class UserFriend(
    val user: User,
    val beFriendTime: Long
)