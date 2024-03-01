package com.example.lunimary.ui.login

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
import androidx.navigation.compose.composable
import com.example.lunimary.R
import com.example.lunimary.design.LocalSnackbarHostState
import com.example.lunimary.design.LunimaryScreen
import com.example.lunimary.design.LunimaryToolbar
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.network.asError
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun NavGraphBuilder.loginScreen(appState: LunimaryAppState, coroutineScope: CoroutineScope) {
    composable(Screens.Login.route) {
        LoginScreen(
            onBack = {
                appState.popBackStack()
            },
            onRegisterClick = { username, password ->

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
    onRegisterClick: (username: String, password: String) -> Unit,
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
    LunimaryScreen(openLoadingWheelDialog = loginState is NetworkResult.Loading) {
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
                    TextButton(onClick = {
                        onRegisterClick(username.value, password.value)
                    }) {
                        Text(
                            text = stringResource(id = R.string.register),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
            LoginScreenContent(
                username = username,
                password = password,
                coroutineScope = coroutineScope,
                onLoginClick = { username, password ->
                    userViewModel.login(username, password)
                }
            )
        }
    }
}
