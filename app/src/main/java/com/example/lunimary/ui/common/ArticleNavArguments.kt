package com.example.lunimary.ui.common

import com.example.lunimary.base.LRUCache
import com.example.lunimary.models.Article

object ArticleNavArguments : LRUCache<Article>(capacity = 5)

const val EDIT_DRAFT_ARTICLE_KEY = "edit_draft_article_key"

const val BROWSE_ARTICLE_KEY = "browse_article_key"