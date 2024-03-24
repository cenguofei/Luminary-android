package com.example.lunimary.models.ext

import com.example.lunimary.models.User
import kotlinx.serialization.Serializable

@Serializable
data class FollowInfo(
    val myFollow: User,
    val alsoFollowMe: Boolean
)
