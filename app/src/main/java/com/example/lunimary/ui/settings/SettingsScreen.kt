package com.example.lunimary.ui.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens

fun NavGraphBuilder.settingsScreen(
    appState: LunimaryAppState,
    onShowSnackbar: (msg: String, label: String?) -> Unit
) {
    composable(route = Screens.Settings.route) {
        SettingsScreenRoute(
            appState = appState,
            darkThemeSettingState = appState.darkThemeSettingState,
            onShowSnackbar = onShowSnackbar
        )
    }
}