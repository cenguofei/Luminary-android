package com.example.lunimary.design

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf

data class SnackbarHostStateHolder(
    val snackbarHostState: SnackbarHostState? = null
)

val LocalSnackbarHostState = staticCompositionLocalOf { SnackbarHostStateHolder() }