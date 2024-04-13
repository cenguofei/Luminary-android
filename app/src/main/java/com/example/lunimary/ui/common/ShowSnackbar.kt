package com.example.lunimary.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

@Composable
fun ShowSnackbar(
    message: String
) {
    val showSnackbar = LocalShowSnackbar.current
    showSnackbar.invoke(message, null)
}

val LocalShowSnackbar = compositionLocalOf<SnackbarType> {
    //error("Composition LocalShowSnackbar not present.")
    {_, _ -> }
}

typealias SnackbarType = (String, String?) -> Unit