package com.example.lunimary.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.R
import com.example.lunimary.design.LocalSnackbarHostState
import com.example.lunimary.design.LunimaryScreen
import com.example.lunimary.design.LunimaryToolbar
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.network.asError
import com.example.lunimary.network.asSuccess
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun NavGraphBuilder.registerScreen(
    appState: LunimaryAppState,
    coroutineScope: CoroutineScope
) {
    composable(route = Screens.Register.route) {
        RegisterScreen(
            appState = appState,
            onBack = { appState.popBackStack() },
            coroutineScope = coroutineScope,
        )
    }
}

@Composable
fun RegisterScreen(
    appState: LunimaryAppState,
    onBack: () -> Unit,
    coroutineScope: CoroutineScope
) {
    val userViewModel = appState.userViewModel
    val registerState by userViewModel.registerState.observeAsState()
    val snackbarHostState = LocalSnackbarHostState.current.snackbarHostState
    when(registerState) {
        is NetworkResult.Loading -> { }
        is NetworkResult.Success -> {
            LaunchedEffect(
                key1 = registerState,
                block = { appState.navToLogin() }
            )
            coroutineScope.launch {
                val data = registerState.asSuccess()
                data?.msg?.let {
                    snackbarHostState?.showSnackbar(
                        message = it,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
        is NetworkResult.Error -> {
            coroutineScope.launch {
                val msg = registerState.asError()?.msg
                snackbarHostState?.showSnackbar(
                    message = msg.toString(),
                    duration = SnackbarDuration.Short
                )
            }
        }
        else -> {}
    }
    LunimaryScreen(openLoadingWheelDialog = registerState is NetworkResult.Loading) {
        val password = remember { mutableStateOf("") }
        val username = remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            LunimaryToolbar(onBack = onBack)
            LoginOrRegisterScreenContent(
                username = username,
                password = password,
                coroutineScope = coroutineScope,
                done = { username, password ->
                    userViewModel.register(username, password)
                },
                type = stringResource(id = R.string.register),
                buttonText = stringResource(id = R.string.register)
            )
        }
    }
}