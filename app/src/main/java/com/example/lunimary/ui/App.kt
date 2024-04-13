package com.example.lunimary.ui

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import com.example.lunimary.R
import com.example.lunimary.base.UserState
import com.example.lunimary.model.User
import com.example.lunimary.ui.browse.browseScreen
import com.example.lunimary.ui.common.LocalShowSnackbar
import com.example.lunimary.ui.edit.addArticleScreen
import com.example.lunimary.ui.login.loginScreen
import com.example.lunimary.ui.login.registerScreen
import com.example.lunimary.ui.privacy.privacyScreen
import com.example.lunimary.ui.relation.relationScreen
import com.example.lunimary.ui.search.searchScreen
import com.example.lunimary.ui.settings.settingsScreen
import com.example.lunimary.ui.topscreens.topLevelScreens
import com.example.lunimary.ui.user.draft.draftsScreen
import com.example.lunimary.ui.user.information.informationScreen
import com.example.lunimary.ui.viewuser.viewUserScreen
import com.example.lunimary.ui.webview.webViewScreen
import com.example.lunimary.util.logd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun LunimaryApp(
    appState: LunimaryAppState,
    startScreen: TopLevelDestination,
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackbar: (String, String?) -> Unit = { message, action ->
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = action,
                duration = SnackbarDuration.Short
            )
        }
    }
    ObserveGlobal(appState = appState, snackbarHostState = snackbarHostState)
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
            CompositionLocalProvider(
                LocalShowSnackbar provides showSnackbar
            ) {
                LunimaryNavHost(
                    appState = appState,
                    startScreen = startScreen,
                    onShowSnackbar = showSnackbar,
                    coroutineScope = coroutineScope
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
private fun LunimaryNavHost(
    modifier: Modifier = Modifier,
    appState: LunimaryAppState,
    onShowSnackbar: (msg: String, label: String?) -> Unit,
    startScreen: TopLevelDestination,
    coroutineScope: CoroutineScope,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startScreen.route,
        modifier = modifier,
    ) {
        topLevelScreens(
            appState = appState,
            onShowSnackbar = onShowSnackbar
        )
        loginScreen(
            appState = appState,
            onShowSnackbar = onShowSnackbar
        )
        settingsScreen(
            appState = appState,
            onShowSnackbar = onShowSnackbar
        )
        registerScreen(
            appState = appState,
            onShowSnackbar = onShowSnackbar
        )
        addArticleScreen(
            appState = appState,
            coroutineScope = coroutineScope,
            onShowSnackbar = onShowSnackbar
        )
        draftsScreen(appState = appState)
        searchScreen(appState = appState)
        webViewScreen(appState = appState)
        browseScreen(appState = appState, onShowSnackbar = onShowSnackbar)
        relationScreen(appState = appState)
        informationScreen(appState = appState, onShowSnackbar = onShowSnackbar)
        viewUserScreen(appState = appState)
        privacyScreen(appState = appState)
    }
}

@Composable
private fun ObserveGlobal(appState: LunimaryAppState, snackbarHostState: SnackbarHostState) {
    val online = appState.userViewModel.online.observeAsState()
    val notConnectedMessage = stringResource(R.string.not_connected_no_retry)
    LaunchedEffect(online.value) {
        if (online.value == false) {
            snackbarHostState.showSnackbar(
                message = notConnectedMessage,
                duration = SnackbarDuration.Short,
            )
        }
        "online=${online.value}".logd("App_is_off_line")
    }
    val userState = UserState.currentUserState.collectAsStateWithLifecycle()
    LaunchedEffect(
        key1 = userState.value,
        block = {
            "App ObserveGlobal collected userState: ${userState.value}".logd("currentUserState")
        }
    )
    if (userState.value == User.NONE && UserState.updated) {
        LaunchedEffect(
            key1 = Unit,
            block = {
                snackbarHostState.showSnackbar("你当前为非登录状态！", null)
            }
        )
    }
    val id = appState.currentDestination?.id
    val route = appState.currentDestination?.route
    val arguments = appState.currentDestination?.arguments
    "currentDestination: id=$id, route=$route, arguments=$arguments".logd("currentDestination")
}