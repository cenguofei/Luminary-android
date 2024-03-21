package com.example.lunimary.design

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.kevinnzou.web.LoadingState
import com.kevinnzou.web.WebView
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewState


const val ChineseMarkdownWeb = "http://markdown.p2hp.com/index.html"

/**
 * https://github.com/KevinnZou/compose-webview-multiplatform?tab=readme-ov-file
 */
@Composable
fun LunimaryWebView(
    url: String,
    showWebView: MutableState<Boolean>,
    onExit: () -> Unit
) {
    val state = rememberWebViewState(url)
    val navigator = rememberWebViewNavigator()

    val goBack = {
        if (navigator.canGoBack) {
            navigator.navigateBack()
        } else {
            showWebView.value = !showWebView.value
            onExit()
        }
    }
    BackHandler { goBack() }
    if (showWebView.value) {
        Column(modifier = Modifier.fillMaxSize()) {
            LunimaryToolbar(
                between = { Text(text = "${state.pageTitle}") },
                onBack = { goBack() },
                modifier = Modifier.statusBarsPadding()
            )
            val loadingState = state.loadingState
            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator(
                    progress = { loadingState.progress },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            WebView(
                state = state,
                navigator = navigator
            )
        }
    }
}