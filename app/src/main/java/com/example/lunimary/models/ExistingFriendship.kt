package com.example.lunimary.models

import kotlinx.serialization.Serializable

@Serializable
data class ExistingFriendship(
    val exists: Boolean = false
)