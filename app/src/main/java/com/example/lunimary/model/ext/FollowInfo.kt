package com.example.lunimary.model.ext

import com.example.lunimary.model.User
import kotlinx.serialization.Serializable

@Serializable
data class FollowInfo(
    val myFollow: User,
    var alsoFollowMe: Boolean
)
