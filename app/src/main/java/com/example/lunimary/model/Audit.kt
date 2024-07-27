package com.example.lunimary.model

import com.example.lunimary.util.Default

@kotlinx.serialization.Serializable
data class Audit(
    val id: Long = Long.Default,

    val auditorId: Long = Long.Default, // …Ûº∆»ÀID

    val articleId: Long = Long.Default,

    val prevState: PublishState = PublishState.Auditing,

    val toState: PublishState = PublishState.Auditing,

    val timestamp: Long = System.currentTimeMillis(),
)
