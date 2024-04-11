package com.example.lunimary.model

import com.example.lunimary.util.Default

/**
 * 我的关注：
 * select * from friends
 *      where myId == Friends.userId
 *
 * 关注我的：
 * select * from friends
 *      where myId == Friends.whoId
 */
@kotlinx.serialization.Serializable
data class Friend(
    val id: Long = Long.Default,

    /**
     * 我的id
     */
    val userId: Long = Long.Default,

    /**
     * 我关注了谁
     */
    val whoId: Long = Long.Default,

    /**
     * 什么时候关注的
     */
    val timestamp: Long = Long.Default
)