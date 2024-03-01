package com.example.lunimary.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import com.example.lunimary.R
import com.example.lunimary.design.LocalSnackbarHostState
import com.example.lunimary.design.SnackbarHostStateHolder
import com.example.lunimary.ui.home.topLevelScreens
import com.example.lunimary.ui.login.loginScreen
import com.example.lunimary.ui.settings.settingsScreen
import com.example.lunimary.util.logd

@Composable
fun LunimaryApp(
    appState: LunimaryAppState,
    startScreen: TopLevelDestination,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()

    // If user is not connected to the internet show a snack bar to inform them.
    val notConnectedMessage = stringResource(R.string.not_connected)
    LaunchedEffect(isOffline) {
        if (isOffline) {
            snackbarHostState.showSnackbar(
                message = notConnectedMessage,
                duration = SnackbarDuration.Indefinite,
            )
            "notConnectedMessage=$notConnectedMessage".logd()
        }
    }
    Scaffold(
        modifier = Modifier,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
        ) {
            CompositionLocalProvider(LocalSnackbarHostState provides SnackbarHostStateHolder(snackbarHostState)) {
                LunimaryNavHost(
                    appState = appState,
                    startScreen = startScreen, onShowSnackbar = { message, action ->
                        snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = action,
                            duration = SnackbarDuration.Short,
                        ) == SnackbarResult.ActionPerformed
                    }
                )
            }
        }
    }
}

@Composable
private fun LunimaryNavHost(
    modifier: Modifier = Modifier,
    appState: LunimaryAppState,
    onShowSnackbar: suspend (msg: String, label: String?) -> Boolean,
    startScreen: TopLevelDestination,
) {
    val coroutineScope = rememberCoroutineScope()
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startScreen.route,
        modifier = modifier,
    ) {
        topLevelScreens(appState = appState)
        loginScreen(appState = appState, coroutineScope = coroutineScope)
        settingsScreen(appState = appState, coroutineScope = coroutineScope)
    }
}
