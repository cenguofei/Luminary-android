package com.example.lunimary.util

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


@Composable
@RequiresApi(api = Build.VERSION_CODES.R)
fun windowInsets() {
    val activity = LocalContext.current as Activity
    val decorView = activity.window.decorView
    decorView.setOnApplyWindowInsetsListener { v, insets ->
        val bottom = insets.getInsets(WindowInsets.Type.ime()).bottom
        decorView.setPadding(0, 0, 0, bottom)
        insets
    }
}