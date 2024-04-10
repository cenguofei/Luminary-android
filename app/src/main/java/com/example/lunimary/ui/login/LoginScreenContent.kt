package com.example.lunimary.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.design.LinearButton
import com.example.lunimary.design.LunimaryDialog

@Composable
fun LoginOrRegisterScreenContent(
    username: MutableState<String>,
    password: MutableState<String>,
    type: String,
    buttonText: String,
    done: (username: String, password: String) -> Unit,
    onNavToProtocol: () -> Unit,
    onShowSnackbar: (msg: String, label: String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = type,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        val agreement = remember { mutableStateOf(false) }
        AccountInput(
            password = password,
            username = username
        )
        Spacer(modifier = Modifier.height(35.dp))
        val openDialog = remember { mutableStateOf(false) }
        LunimaryDialog(
            text = stringResource(id = R.string.agree_privacy_before_login_or_register),
            openDialog = openDialog
        )
        val message = stringResource(id = R.string.pwd_or_username_cannot_empty)
        LinearButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(50.dp),
            onClick = {
                if (username.value.isEmpty() || password.value.isEmpty()) {
                    onShowSnackbar(message, null)
                    return@LinearButton
                }
                if (agreement.value) {
                    done(username.value, password.value)
                } else {
                    openDialog.value = true
                }
            },
            text = buttonText
        )
        Spacer(modifier = Modifier.height(10.dp))
        PrivacyProtocol(
            agreement = agreement,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onNavToProtocol = onNavToProtocol
        )
    }
}