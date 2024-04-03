package com.example.lunimary.design

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class SnackbarHostStateHolder(
    val snackbarHostState: SnackbarHostState? = null
)

val LocalSnackbarHostState = staticCompositionLocalOf { SnackbarHostStateHolder() }

@Composable
fun ShowSnackbar(
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onShowEnd: () -> Unit = {}
) {
    val snackbarHostState = LocalSnackbarHostState.current.snackbarHostState
    coroutineScope.launch {
        snackbarHostState?.showSnackbar(
            message = message,
            duration = duration
        )
        onShowEnd()
    }
}