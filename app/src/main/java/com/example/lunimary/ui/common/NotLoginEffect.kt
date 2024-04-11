package com.example.lunimary.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.lunimary.base.notLogin
import com.example.lunimary.ui.LunimaryAppState

@Composable
fun NotLoginEffect(appState: LunimaryAppState) {
    if (notLogin()) {
        LaunchedEffect(
            key1 = Unit,
            block = { appState.navToLogin() }
        )
    }
}