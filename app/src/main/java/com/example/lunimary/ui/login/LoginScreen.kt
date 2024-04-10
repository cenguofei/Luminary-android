package com.example.lunimary.ui.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.lunimary.R
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.network.asError
import com.example.lunimary.design.LunimaryToolbar
import com.example.lunimary.design.nicepage.LunimaryStateContent
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import com.example.lunimary.util.unknownErrorMsg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun NavGraphBuilder.loginScreen(
    appState: LunimaryAppState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: (msg: String, label: String?) -> Unit
) {
    composable(
        Screens.Login.route,
        arguments = listOf(
            navArgument("fromNeedLogin") {
                defaultValue = true
                type = NavType.BoolType
            }
        )
    ) { navBackEntry ->
        val fromNeedLogin = navBackEntry.arguments?.getBoolean("fromNeedLogin") ?: false
        BackHandler {
            if (fromNeedLogin) {
                appState.navToHome(Screens.Login.route)
            } else {
                appState.popBackStack()
            }
        }
        LoginScreen(
            onBack = {
                if (fromNeedLogin) {
                    appState.navToHome(Screens.Login.route)
                } else {
                    appState.popBackStack()
                }
            },
            onRegisterClick = {
                appState.navToRegister()
            },
            coroutineScope = coroutineScope,
            userViewModel = appState.userViewModel,
            appState = appState,
            onShowSnackbar = onShowSnackbar
        )
    }
}



@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onRegisterClick: () -> Unit,
    coroutineScope: CoroutineScope,
    userViewModel: UserViewModel,
    appState: LunimaryAppState,
    onShowSnackbar: (msg: String, label: String?) -> Unit,
) {
    val loginState by userViewModel.loginState.observeAsState()
    val notConnected = stringResource(id = R.string.not_connected)
    LaunchedEffect(
        key1 = loginState,
        block = {
            when(loginState) {
                is NetworkResult.Success -> {
                    appState.navToHome(Screens.Login.route)
                }
                is NetworkResult.Error -> {
                    coroutineScope.launch {
                        loginState.asError()?.msg?.let {
                            val message = it.ifEmpty {
                                if (userViewModel.online.value == true) {
                                    unknownErrorMsg
                                } else {
                                    notConnected
                                }
                            }
                            onShowSnackbar(message, null)
                        }
                    }
                }
                else -> {}
            }
        }
    )
    LunimaryStateContent(openLoadingWheelDialog = loginState is NetworkResult.Loading) {
        val password = remember { mutableStateOf("") }
        val username = remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            LunimaryToolbar(
                onBack = onBack,
                end = {
                    TextButton(onClick = { onRegisterClick() }) {
                        Text(
                            text = stringResource(id = R.string.register),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
            LoginOrRegisterScreenContent(
                username = username,
                password = password,
                done = { username, password ->
                    userViewModel.login(username, password)
                },
                type = stringResource(id = R.string.password_login),
                buttonText = stringResource(id = R.string.login),
                onNavToProtocol = appState::navToPrivacy,
                onShowSnackbar = onShowSnackbar
            )
        }
    }
}
