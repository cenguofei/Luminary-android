package com.example.lunimary.ui.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens

fun NavGraphBuilder.registerScreen(
    appState: LunimaryAppState,
    onShowSnackbar: (msg: String, label: String?) -> Unit
) {
    composable(route = Screens.Register.route) {
        RegisterScreenRoute(
            appState = appState,
            onBack = { appState.popBackStack() },
            onShowSnackbar = onShowSnackbar
        )
    }
}