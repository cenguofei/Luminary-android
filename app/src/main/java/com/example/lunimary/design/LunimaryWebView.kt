package com.example.lunimary.design

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.lunimary.util.empty
import com.example.lunimary.util.logd
import com.kevinnzou.web.WebView
import com.kevinnzou.web.WebViewState
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewState


const val ChineseMarkdownWeb = "http://markdown.p2hp.com/index.html"

/**
 * https://github.com/KevinnZou/compose-webview-multiplatform?tab=readme-ov-file
 */
@Composable
fun LunimaryWebView(
    url: String = empty,
    onExit: () -> Unit = {},
    state: WebViewState = rememberWebViewState(url),
    showToolbar: Boolean = true
) {
    val navigator = rememberWebViewNavigator()

    val goBack = {
        if (navigator.canGoBack) {
            navigator.navigateBack()
        } else {
            onExit()
        }
    }
    BackHandler { goBack() }
    Column(modifier = Modifier.fillMaxSize()) {
        if (showToolbar) {
            LunimaryToolbar(
                between = { Text(text = "${state.pageTitle}") },
                onBack = { goBack() },
                modifier = Modifier.statusBarsPadding()
            )
        }
//        if (state.isLoading) {
//            val indicatorModifier = if (showToolbar) Modifier else Modifier.statusBarsPadding()
//            LinearProgressIndicator(
//                modifier = indicatorModifier.fillMaxWidth(),
//                color = MaterialTheme.colorScheme.primary
//            )
//        }
        WebView(
            state = state,
            navigator = navigator,
            factory = {
                android.webkit.WebView(it).apply {
                    val userAgentString = settings.userAgentString
                    "userAgentString=$userAgentString".logd("user_agent")
                    val systemUserAgent = System.getProperty("http.agent")
                    "systemUserAgent=$systemUserAgent".logd("user_agent")
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}