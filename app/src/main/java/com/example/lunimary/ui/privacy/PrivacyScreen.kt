package com.example.lunimary.ui.privacy

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.base.mmkv.DarkThemeSetting
import com.example.lunimary.base.mmkv.SettingMMKV
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.network.asError
import com.example.lunimary.base.network.asSuccess
import com.example.lunimary.design.LunimaryStateContent
import com.example.lunimary.design.LunimaryToolbar
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import com.example.lunimary.util.empty
import dev.jeziellago.compose.markdowntext.MarkdownText

fun NavGraphBuilder.privacyScreen(appState: LunimaryAppState) {
    composable(
        route = Screens.PrivacyProtocol.route
    ) {
        val viewModel: PrivacyViewModel = viewModel()
        PrivacyScreen(
            onBack = appState::popBackStack,
            viewModel = viewModel
        )
    }
}

@Composable
fun PrivacyScreen(onBack: () -> Unit, viewModel: PrivacyViewModel) {
    val data = viewModel.privacy.value
    Column(modifier = Modifier.fillMaxSize()) {
        LunimaryToolbar(
            onBack = onBack,
            modifier = Modifier.statusBarsPadding()
        )
        when(data) {
            is NetworkResult.Loading -> {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            else -> { }
        }
        LunimaryStateContent(
            error = data is NetworkResult.Error,
            errorMsg = data.asError()?.msg
        ) {
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)) {
                item {
                    val systemDarkMode = isSystemInDarkTheme()
                    MarkdownText(
                        modifier = Modifier,
                        markdown = data.asSuccess()?.data ?: empty,
                        linkColor = when {
                            SettingMMKV.userHasSetTheme -> {
                                if (SettingMMKV.darkThemeSetting == DarkThemeSetting.DarkMode) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    Color.Blue
                                }
                            }

                            systemDarkMode -> MaterialTheme.colorScheme.primary
                            else -> Color.Blue
                        },
                        style = LocalTextStyle.current.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    )
                }
            }
        }
    }
}