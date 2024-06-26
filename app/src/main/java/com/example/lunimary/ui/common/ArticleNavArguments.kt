package com.example.lunimary.ui.common

import com.example.lunimary.base.LRUCache
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.model.Article

object ArticleNavArguments : LRUCache<Any>(capacity = 5)

const val EDIT_ARTICLE_KEY = "edit_article_key"
const val EDIT_TYPE_KEY = "edit_type_key"

object PageArticleNavArguments : LRUCache<PageItem<Article>>(capacity = 5)

const val PAGE_ARTICLE_ITEM_KEY = "page_article_item_key"