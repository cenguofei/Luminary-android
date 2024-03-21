package com.example.lunimary.ui.common

import com.example.lunimary.base.LRUCache

object UrlNavArguments : LRUCache<String>(capacity = 5)

const val WEB_VIEW_URL_KEY = "web_view_url_key"

const val DEFAULT_WEB_URL = "http://localhost:8080/index"