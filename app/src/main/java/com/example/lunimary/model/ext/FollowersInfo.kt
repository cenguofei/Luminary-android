package com.example.lunimary.model.ext

import com.example.lunimary.model.User
import kotlinx.serialization.Serializable

@Serializable
data class FollowersInfo(
    val follower: User,
    var alsoFollow: Boolean
)
