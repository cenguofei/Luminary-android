package com.example.lunimary.ui.common

import com.example.lunimary.base.LRUCache

object FriendsPageTypeNavArguments : LRUCache<RelationPageType>(capacity = 5)

const val PAGE_INDEX_KEY = "page_index_key"

fun getRelationPage(): RelationPageType {
    return FriendsPageTypeNavArguments[PAGE_INDEX_KEY] ?: RelationPageType.Friends
}

fun setRelationPage(page: RelationPageType) {
    FriendsPageTypeNavArguments[PAGE_INDEX_KEY] = page
}

enum class RelationPageType(val pageName: String) {
    Friends("朋友"),
    Follow("关注"),
    Followers("粉丝")
}