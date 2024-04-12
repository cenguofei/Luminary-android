package com.example.lunimary.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.lunimary.R
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.network.asError
import com.example.lunimary.base.network.asSuccess
import com.example.lunimary.design.LunimaryToolbar
import com.example.lunimary.design.nicepage.LunimaryStateContent
import com.example.lunimary.ui.LunimaryAppState

@Composable
fun RegisterScreenRoute(
    appState: LunimaryAppState,
    onBack: () -> Unit,
    onShowSnackbar: (msg: String, label: String?) -> Unit
) {
    val userViewModel = appState.userViewModel
    val registerState by userViewModel.registerState.observeAsState()
    when (registerState) {
        is NetworkResult.Loading -> {}
        is NetworkResult.Success -> {
            LaunchedEffect(
                key1 = registerState,
                block = {
                    appState.navToLogin()
                    userViewModel.resetRegisterState()
                    val data = registerState.asSuccess()
                    data?.msg?.let {
                        onShowSnackbar(it, null)
                    }
                }
            )
        }

        is NetworkResult.Error -> {
            LaunchedEffect(
                key1 = Unit,
                block = {
                    val msg = registerState.asError()?.msg
                    onShowSnackbar(msg.toString(), null)
                }
            )
        }

        else -> {}
    }
    LunimaryStateContent(openLoadingWheelDialog = registerState is NetworkResult.Loading) {
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
                type = stringResource(id = R.string.register),
                buttonText = stringResource(id = R.string.register),
                done = { username, password ->
                    val verify = verifyInput(username, password)
                    if (verify.first) {
                        userViewModel.register(username, password)
                    } else {
                        onShowSnackbar(verify.second, null)
                    }
                },
                onNavToProtocol = appState::navToPrivacy,
                onShowSnackbar = onShowSnackbar
            )
        }
    }
}