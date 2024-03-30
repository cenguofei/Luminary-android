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
import com.example.lunimary.design.LocalSnackbarHostState
import com.example.lunimary.design.LunimaryStateContent
import com.example.lunimary.design.LunimaryToolbar
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.network.asError
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun NavGraphBuilder.loginScreen(appState: LunimaryAppState, coroutineScope: CoroutineScope) {
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
                appState.navToHome()
            } else {
                appState.popBackStack()
            }
        }
        LoginScreen(
            onBack = {
                if (fromNeedLogin) {
                    appState.navToHome()
                } else {
                    appState.popBackStack()
                }
            },
            onRegisterClick = {
                appState.navToRegister()
            },
            coroutineScope = coroutineScope,
            userViewModel = appState.userViewModel,
            appState = appState
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
) {
    val loginState by userViewModel.loginState.observeAsState()
    val snackbarHostState = LocalSnackbarHostState.current.snackbarHostState
    LaunchedEffect(
        key1 = loginState,
        block = {
            when(loginState) {
                is NetworkResult.Success -> {
                    appState.navToHome()
                }
                is NetworkResult.Error -> {
                    coroutineScope.launch {
                        loginState.asError()?.msg?.let {
                            snackbarHostState?.showSnackbar(message = it)
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
                coroutineScope = coroutineScope,
                done = { username, password ->
                    userViewModel.login(username, password)
                },
                type = stringResource(id = R.string.password_login),
                buttonText = stringResource(id = R.string.login)
            )
        }
    }
}
