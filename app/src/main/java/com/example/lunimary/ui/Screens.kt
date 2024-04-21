package com.example.lunimary.ui

enum class Screens(val route: String) {
    Login("$LOGIN_ROOT/{fromNeedLogin}"),
    Register("register"),
    Settings("settings"),
    AddArticle("$ADD_ARTICLE_ROOT?article={draftArticle}"),
    Search("search"),
    Drafts("drafts"),
    WebView(WEB_VIEW_ROOT),
    BrowseArticle(BROWSE_ARTICLE_ROOT),
    Relation("relation"),
    Information("user/information"),
    ViewUser("view_user"),
    PrivacyProtocol("privacy"),
    TopicSelect("topic_select")
}

const val LOGIN_ROOT = "login"
const val ADD_ARTICLE_ROOT = "add_article"
const val WEB_VIEW_ROOT = "webView"
const val BROWSE_ARTICLE_ROOT = "browse_article"