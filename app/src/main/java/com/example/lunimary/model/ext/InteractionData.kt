package com.example.lunimary.model.ext

@kotlinx.serialization.Serializable
data class InteractionData(
    val likeNum: Long = 0, //获赞数量
    val friendNum: Long = 0, //朋友数量
    val followingNum: Long = 0, //关注人数
    val followerNum: Long = 0, //粉丝数量
)
