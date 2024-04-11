package com.example.lunimary.model

import kotlinx.serialization.Serializable

@Serializable
data class ExistingFriendship(
    val exists: Boolean = false
)