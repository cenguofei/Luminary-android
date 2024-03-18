package com.example.lunimary.ui.message

enum class MessagePageType(val pageName: String) {
    Comment("评论"),
    Like("赞"),
    Follow("关注")
}

val messagePages: List<MessagePageType> = listOf(
    MessagePageType.Comment,
    MessagePageType.Like,
    MessagePageType.Follow
)