package com.example.lunimary.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.R
import com.example.lunimary.base.mmkv.DarkThemeSetting
import com.example.lunimary.design.LinearButton
import com.example.lunimary.design.LoadingDialog
import com.example.lunimary.design.LocalSnackbarHostState
import com.example.lunimary.design.LunimaryToolbar
import com.example.lunimary.models.User
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.network.asError
import com.example.lunimary.base.network.isError
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import com.example.lunimary.base.currentUser
import com.example.lunimary.util.logd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun NavGraphBuilder.settingsScreen(appState: LunimaryAppState, coroutineScope: CoroutineScope) {
    composable(route = Screens.Settings.route) {
        SettingsScreen(
            appState = appState,
            coroutineScope = coroutineScope,
            darkThemeSettingState = appState.darkThemeSettingState
        )
    }
}

@Composable
fun SettingsScreen(
    appState: LunimaryAppState,
    coroutineScope: CoroutineScope,
    darkThemeSettingState: State<DarkThemeSetting>,
) {
    val logoutState by appState.userViewModel.logoutState.observeAsState()
    val snackbarHostState = LocalSnackbarHostState.current.snackbarHostState
    val showLoadingWheel = remember { mutableStateOf(false) }

    when (logoutState) {
        is NetworkResult.Loading -> {
            showLoadingWheel.value = true
        }

        is NetworkResult.None -> {}
        is NetworkResult.Success -> {
            showLoadingWheel.value = false
            appState.userViewModel.reset()
            appState.navToHome(Screens.Settings.route)
        }

        is NetworkResult.Error -> {
            showLoadingWheel.value = false
            if (logoutState.isError()) {
                logoutState.asError()?.msg.let {
                    it.logd()
                    coroutineScope.launch {
                        if (it != null) {
                            snackbarHostState?.showSnackbar(
                                message = it,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            }
        }

        else -> {

        }
    }

    LoadingDialog(show = showLoadingWheel.value)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        LunimaryToolbar(
            onBack = { appState.popBackStack() },
            between = {
                Text(
                    text = stringResource(id = R.string.settings),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        SettingsItems(
            modifier = Modifier.weight(1f),
            darkThemeSettingState = darkThemeSettingState,
            onThemeSettingChange = appState::onThemeSettingChange
        )
        if (currentUser != User.NONE) {
            LinearButton(
                onClick = {
                    appState.userViewModel.logout()
                },
                text = stringResource(id = R.string.logout),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(45.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}