package com.example.lunimary.ui.webview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.lunimary.design.ChineseMarkdownWeb
import com.example.lunimary.design.LunimaryWebView
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import com.example.lunimary.ui.common.DEFAULT_WEB_URL
import com.example.lunimary.ui.common.UrlNavArguments
import com.example.lunimary.ui.common.WEB_VIEW_URL_KEY
import com.example.lunimary.util.Default

fun NavGraphBuilder.webViewScreen(appState: LunimaryAppState) {
    composable(
        route = Screens.WebView.route
    ) {
        val url = UrlNavArguments[WEB_VIEW_URL_KEY] ?: DEFAULT_WEB_URL
        WebViewScreen(
            url = url,
            onExit = { appState.popBackStack() }
        )
    }
}

@Composable
private fun WebViewScreen(url: String, onExit: () -> Unit) {
    val showWebView: MutableState<Boolean> = remember { mutableStateOf(true) }
    if (showWebView.value) {
        LunimaryWebView(
            url = url,
            showWebView = showWebView,
            onExit = onExit
        )
    }
}